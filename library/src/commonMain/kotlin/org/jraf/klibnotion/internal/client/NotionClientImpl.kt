/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2021-present Benoit 'BoD' Lubek (BoD@JRAF.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jraf.klibnotion.internal.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.UserAgent
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.readText
import io.ktor.http.URLBuilder
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.json.Json
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.HttpLoggingLevel
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.internal.api.model.apiToModel
import org.jraf.klibnotion.internal.api.model.database.ApiDatabaseConverter
import org.jraf.klibnotion.internal.api.model.database.query.ApiDatabaseQueryConverter
import org.jraf.klibnotion.internal.api.model.modelToApi
import org.jraf.klibnotion.internal.api.model.page.ApiCreateTableParametersConverter
import org.jraf.klibnotion.internal.api.model.page.ApiPageConverter
import org.jraf.klibnotion.internal.api.model.page.ApiPageResultPageConverter
import org.jraf.klibnotion.internal.api.model.page.ApiUpdateTableParametersConverter
import org.jraf.klibnotion.internal.api.model.user.ApiUserConverter
import org.jraf.klibnotion.internal.api.model.user.ApiUserResultPageConverter
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.database.Database
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.database.query.DatabaseQuerySort
import org.jraf.klibnotion.model.exceptions.NotionClientException
import org.jraf.klibnotion.model.exceptions.NotionClientRequestException
import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.pagination.Pagination
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.value.PropertyValueList
import org.jraf.klibnotion.model.user.User

internal class NotionClientImpl(
    clientConfiguration: ClientConfiguration,
) : NotionClient,
    NotionClient.Users,
    NotionClient.Databases,
    NotionClient.Pages {

    override val users = this
    override val databases = this
    override val pages = this

    @OptIn(KtorExperimentalAPI::class)
    private val httpClient by lazy {
        createHttpClient(clientConfiguration.httpConfiguration.bypassSslChecks) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    Json {
                        // XXX Comment this to have API changes make the parsing fail
                        ignoreUnknownKeys = true

                        // This is needed to accept JSON Numbers to be deserialized as Strings
                        isLenient = true
                    }
                )
            }
            defaultRequest {
                header(
                    "Authorization",
                    "Bearer ${clientConfiguration.authentication.apiToken}"
                )
            }
            install(UserAgent) {
                agent = clientConfiguration.userAgent
            }
            // Notion API is very slow, so...
            install(HttpTimeout) {
                requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
                connectTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
                socketTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
            }
            engine {
                // Setup a proxy if requested
                clientConfiguration.httpConfiguration.httpProxy?.let { httpProxy ->
                    proxy = ProxyBuilder.http(URLBuilder().apply {
                        host = httpProxy.host
                        port = httpProxy.port
                    }.build())
                }
            }
            // Setup logging if requested
            if (clientConfiguration.httpConfiguration.loggingLevel != HttpLoggingLevel.NONE) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = when (clientConfiguration.httpConfiguration.loggingLevel) {
                        HttpLoggingLevel.NONE -> LogLevel.NONE
                        HttpLoggingLevel.INFO -> LogLevel.INFO
                        HttpLoggingLevel.HEADERS -> LogLevel.HEADERS
                        HttpLoggingLevel.BODY -> LogLevel.BODY
                        HttpLoggingLevel.ALL -> LogLevel.ALL
                    }
                }
            }
            HttpResponseValidator {
                handleResponseException { cause: Throwable ->
                    if (cause is ClientRequestException) throw NotionClientRequestException(
                        cause,
                        cause.response.readText()
                    )
                    throw NotionClientException(cause)
                }
            }
        }
    }

    private val service: NotionService by lazy {
        NotionService(httpClient)
    }

    // region Users

    override suspend fun getUser(id: UuidString): User {
        return service.getUser(id)
            .apiToModel(ApiUserConverter)
    }

    override suspend fun getUserList(pagination: Pagination): ResultPage<User> {
        return service.getUserList(pagination.startCursor)
            .apiToModel(ApiUserResultPageConverter)
    }

    // endregion


    // region Databases

    override suspend fun getDatabase(id: UuidString): Database {
        return service.getDatabase(id)
            .apiToModel(ApiDatabaseConverter)
    }

    override suspend fun queryDatabase(
        id: UuidString,
        query: DatabaseQuery?,
        sort: DatabaseQuerySort?,
        pagination: Pagination,
    ): ResultPage<Page> {
        return service.queryDatabase(
            id,
            (query to sort).modelToApi(ApiDatabaseQueryConverter),
            pagination.startCursor
        )
            .apiToModel(ApiPageResultPageConverter)
    }

    // endregion


    // region Pages

    override suspend fun getPage(id: UuidString, isArchived: Boolean): Page {
        return service.getPage(id, isArchived)
            .apiToModel(ApiPageConverter)
    }

    override suspend fun createPage(parentDatabaseId: UuidString, properties: PropertyValueList): Page {
        return service.createPage((parentDatabaseId to properties.propertyValueList).modelToApi(
            ApiCreateTableParametersConverter))
            .apiToModel(ApiPageConverter)
    }

    override suspend fun updatePage(id: UuidString, properties: PropertyValueList): Page {
        return service.updatePage(id, properties.propertyValueList.modelToApi(ApiUpdateTableParametersConverter))
            .apiToModel(ApiPageConverter)
    }

    // endregion


    override fun close() = httpClient.close()
}

internal expect fun createHttpClient(
    bypassSslChecks: Boolean,
    block: HttpClientConfig<*>.() -> Unit,
): HttpClient


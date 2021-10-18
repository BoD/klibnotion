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
 * Copyright (C) 2021-present Yu Jinyan (i@yujinyan.me)
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
import io.ktor.http.HttpHeaders
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.HttpLoggingLevel
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.internal.api.model.apiToModel
import org.jraf.klibnotion.internal.api.model.block.ApiAppendBlocksParametersConverter
import org.jraf.klibnotion.internal.api.model.block.ApiInBlockConverter
import org.jraf.klibnotion.internal.api.model.block.ApiOutBlockConverter
import org.jraf.klibnotion.internal.api.model.block.ApiPageResultBlockConverter
import org.jraf.klibnotion.internal.api.model.database.ApiDatabaseConverter
import org.jraf.klibnotion.internal.api.model.database.create.ApiDatabaseCreateParametersConverter
import org.jraf.klibnotion.internal.api.model.database.create.DatabaseCreateParameters
import org.jraf.klibnotion.internal.api.model.database.query.ApiDatabaseQueryConverter
import org.jraf.klibnotion.internal.api.model.database.update.ApiDatabaseUpdateParametersConverter
import org.jraf.klibnotion.internal.api.model.database.update.DatabaseUpdateParameters
import org.jraf.klibnotion.internal.api.model.modelToApi
import org.jraf.klibnotion.internal.api.model.oauth.ApiOAuthGetAccessTokenParameters
import org.jraf.klibnotion.internal.api.model.oauth.ApiOAuthGetAccessTokenResultConverter
import org.jraf.klibnotion.internal.api.model.page.ApiPageConverter
import org.jraf.klibnotion.internal.api.model.page.ApiPageCreateParametersConverter
import org.jraf.klibnotion.internal.api.model.page.ApiPageResultDatabaseConverter
import org.jraf.klibnotion.internal.api.model.page.ApiPageResultPageConverter
import org.jraf.klibnotion.internal.api.model.page.ApiPageUpdateParametersConverter
import org.jraf.klibnotion.internal.api.model.page.PageCreateParameters
import org.jraf.klibnotion.internal.api.model.page.PageUpdateParameters
import org.jraf.klibnotion.internal.api.model.search.ApiSearchParametersConverter
import org.jraf.klibnotion.internal.api.model.user.ApiUserConverter
import org.jraf.klibnotion.internal.api.model.user.ApiUserResultPageConverter
import org.jraf.klibnotion.internal.klibNotionScope
import org.jraf.klibnotion.internal.model.block.MutableBlock
import org.jraf.klibnotion.internal.model.oauth.OAuthCodeAndStateImpl
import org.jraf.klibnotion.model.base.EmojiOrFile
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.base.reference.DatabaseReference
import org.jraf.klibnotion.model.base.reference.PageReference
import org.jraf.klibnotion.model.block.Block
import org.jraf.klibnotion.model.block.BlockListProducer
import org.jraf.klibnotion.model.block.MutableBlockList
import org.jraf.klibnotion.model.block.invoke
import org.jraf.klibnotion.model.database.Database
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.exceptions.NotionClientException
import org.jraf.klibnotion.model.exceptions.NotionClientRequestException
import org.jraf.klibnotion.model.file.File
import org.jraf.klibnotion.model.oauth.OAuthCodeAndState
import org.jraf.klibnotion.model.oauth.OAuthCredentials
import org.jraf.klibnotion.model.oauth.OAuthGetAccessTokenResult
import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.pagination.Pagination
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.sort.PropertySort
import org.jraf.klibnotion.model.property.spec.PropertySpecList
import org.jraf.klibnotion.model.property.value.PropertyValueList
import org.jraf.klibnotion.model.richtext.RichTextList
import org.jraf.klibnotion.model.user.User
import kotlin.coroutines.coroutineContext

internal class NotionClientImpl(
    clientConfiguration: ClientConfiguration,
) : NotionClient,
    NotionClient.OAuth,
    NotionClient.Users,
    NotionClient.Databases,
    NotionClient.Pages,
    NotionClient.Blocks,
    NotionClient.Search {

    override val oAuth = this
    override val users = this
    override val databases = this
    override val pages = this
    override val blocks = this
    override val search = this

    private val httpClient by lazy {
        createHttpClient(clientConfiguration.httpConfiguration.bypassSslChecks) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    Json {
                        // XXX Comment this to have API changes make the parsing fail
                        ignoreUnknownKeys = true

                        // This is needed to accept JSON Numbers to be deserialized as Strings
                        isLenient = true

                        // This may improve performance
                        // PLUS is a workaround for these issues:
                        // - https://youtrack.jetbrains.com/issue/KTOR-2740
                        // - https://github.com/Kotlin/kotlinx.serialization/issues/1450
                        useAlternativeNames = false
                    }
                )
            }
            defaultRequest {
                if (headers[HttpHeaders.Authorization] == null) {
                    val authentication = clientConfiguration.authentication
                    if (!authentication.isSet) throw IllegalStateException("You must set the Authentication accessToken before making this call")
                    header(
                        HttpHeaders.Authorization,
                        "Bearer ${authentication.accessToken}"
                    )
                }
                header(HEADER_NOTION_VERSION, NOTION_API_VERSION)
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

    // region OAuth

    override fun getUserPromptUri(oAuthCredentials: OAuthCredentials, uniqueState: String): String {
        return URLBuilder(protocol = URLProtocol.createOrDefault(NotionService.OAUTH_URL_SCHEME),
            host = NotionService.OAUTH_URL_HOST,
            encodedPath = NotionService.OAUTH_URL_PATH,
            parameters = ParametersBuilder().apply {
                append("client_id", oAuthCredentials.clientId)
                append("redirect_uri", oAuthCredentials.redirectUri)
                append("response_type", "code")
                // XXX Adding a _ to ensure it's not interpreted as a number
                append("state", "_$uniqueState")
            }
        ).buildString()
    }

    override fun extractCodeAndStateFromRedirectUri(redirectUri: String): OAuthCodeAndState? {
        return try {
            val url = Url(redirectUri)
            // XXX Removing the _ added in getLoginUri
            val state = url.parameters["state"]!!.removePrefix("_")
            OAuthCodeAndStateImpl(code = url.parameters["code"]!!, state = state)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAccessToken(oAuthCredentials: OAuthCredentials, code: String): OAuthGetAccessTokenResult {
        return service.getOAuthAccessToken(
            clientId = oAuthCredentials.clientId,
            clientSecret = oAuthCredentials.clientSecret,
            parameters = ApiOAuthGetAccessTokenParameters(
                grant_type = "authorization_code",
                redirect_uri = oAuthCredentials.redirectUri,
                code = code,
            )
        )
            .apiToModel(ApiOAuthGetAccessTokenResultConverter)
    }

    // endregion


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

    override suspend fun getDatabaseList(pagination: Pagination): ResultPage<Database> {
        return service.getDatabaseList(pagination.startCursor)
            .apiToModel(ApiPageResultDatabaseConverter)
    }

    override suspend fun queryDatabase(
        id: UuidString,
        query: DatabaseQuery?,
        sort: PropertySort?,
        pagination: Pagination,
    ): ResultPage<Page> {
        return service.queryDatabase(
            id,
            (query to sort).modelToApi(ApiDatabaseQueryConverter),
            pagination.startCursor
        )
            .apiToModel(ApiPageResultPageConverter)
    }

    override suspend fun createDatabase(
        parentPageId: UuidString,
        title: RichTextList,
        icon: EmojiOrFile?,
        cover: File?,
        properties: PropertySpecList,
    ): Database {
        return service.createDatabase(
            DatabaseCreateParameters(
                parentPageId = parentPageId,
                title = title,
                icon = icon,
                cover = cover,
                properties = properties,
            ).modelToApi(ApiDatabaseCreateParametersConverter)
        )
            .apiToModel(ApiDatabaseConverter)
    }

    override suspend fun updateDatabase(
        id: UuidString,
        title: RichTextList?,
        icon: EmojiOrFile?,
        cover: File?,
        properties: PropertySpecList?,
    ): Database {
        return service.updateDatabase(
            id,
            DatabaseUpdateParameters(
                title = title,
                icon = icon,
                cover = cover,
                properties = properties,
            ).modelToApi(ApiDatabaseUpdateParametersConverter)
        )
            .apiToModel(ApiDatabaseConverter)
    }

    // endregion


    // region Pages

    override suspend fun getPage(id: UuidString): Page {
        return service.getPage(id)
            .apiToModel(ApiPageConverter)
    }

    override suspend fun createPage(
        parentDatabase: DatabaseReference,
        icon: EmojiOrFile?,
        cover: File?,
        properties: PropertyValueList,
        content: MutableBlockList?,
    ): Page {
        return service.createPage(
            PageCreateParameters(
                reference = parentDatabase,
                properties = properties.propertyValueList,
                children = content,
                icon = icon,
                cover = cover,
            ).modelToApi(ApiPageCreateParametersConverter)
        )
            .apiToModel(ApiPageConverter)
    }

    override suspend fun createPage(
        parentDatabase: DatabaseReference,
        icon: EmojiOrFile?,
        cover: File?,
        properties: PropertyValueList,
        content: BlockListProducer,
    ): Page = createPage(
        parentDatabase = parentDatabase,
        properties = properties,
        icon = icon,
        cover = cover,
        content = content(),
    )

    override suspend fun createPage(
        parentPage: PageReference,
        title: RichTextList,
        icon: EmojiOrFile?,
        cover: File?,
        content: MutableBlockList?,
    ): Page {
        return service.createPage(
            PageCreateParameters(
                reference = parentPage,
                properties = PropertyValueList().title("title", title).propertyValueList,
                children = content,
                icon = icon,
                cover = cover
            ).modelToApi(ApiPageCreateParametersConverter)
        )
            .apiToModel(ApiPageConverter)
    }

    override suspend fun createPage(
        parentPage: PageReference,
        title: RichTextList,
        icon: EmojiOrFile?,
        cover: File?,
        content: BlockListProducer,
    ): Page = createPage(
        parentPage = parentPage,
        title = title,
        icon = icon,
        cover = cover,
        content = content(),
    )

    override suspend fun updatePage(
        id: UuidString,
        icon: EmojiOrFile?,
        cover: File?,
        properties: PropertyValueList,
    ): Page {
        return service.updatePage(
            id = id,
            parameters = PageUpdateParameters(
                properties = properties.propertyValueList,
                icon = icon,
                cover = cover,
            ).modelToApi(ApiPageUpdateParametersConverter)
        )
            .apiToModel(ApiPageConverter)
    }

    override suspend fun setPageArchived(id: UuidString, archived: Boolean): Page {
        return service.archivePage(id, archived)
            .apiToModel(ApiPageConverter)
    }

    // endregion


    // region Blocks

    override suspend fun getBlockList(parentId: UuidString, pagination: Pagination): ResultPage<Block> {
        return service.getBlockList(parentId, pagination.startCursor)
            .apiToModel(ApiPageResultBlockConverter)
    }

    override suspend fun getAllBlockListRecursively(parentId: UuidString): List<Block> {
        val results = mutableListOf<Block>()
        var nextPagination: Pagination? = Pagination()
        while (nextPagination != null) {
            val blockResultPage = service.getBlockList(parentId, nextPagination.startCursor)
                .apiToModel(ApiPageResultBlockConverter)
            getChildrenRecursively(blockResultPage)
            results += blockResultPage.results
            nextPagination = blockResultPage.nextPagination
        }
        return results
    }

    private suspend fun getChildrenRecursively(blockResultPage: ResultPage<Block>) {
        val job = Job()
        for (block in blockResultPage.results) {
            if (block is MutableBlock && block.children?.isEmpty() == true) {
                @Suppress("DeferredResultUnused")
                klibNotionScope.async(coroutineContext + job) {
                    val childrenResultPage = getAllBlockListRecursively(block.id)
                    block.children = childrenResultPage
                }
            }
        }
        job.children.forEach { it.join() }
    }

    override suspend fun appendBlockList(parentId: UuidString, blocks: MutableBlockList) {
        service.appendBlockList(parentId, blocks.modelToApi(ApiAppendBlocksParametersConverter))
    }

    override suspend fun appendBlockList(parentId: UuidString, blocks: BlockListProducer) =
        appendBlockList(parentId, blocks() ?: MutableBlockList())

    override suspend fun getBlock(id: UuidString, retrieveChildrenRecursively: Boolean): Block {
        val block = service.getBlock(id)
            .apiToModel(ApiInBlockConverter)
        if (retrieveChildrenRecursively && block.children != null) {
            val children = getAllBlockListRecursively(id)
            (block as MutableBlock).children = children
        }
        return block
    }

    override suspend fun updateBlock(id: UuidString, block: Block): Block {
        return service.updateBlock(id, block.modelToApi(ApiOutBlockConverter))
            .apiToModel(ApiInBlockConverter)
    }

    // endregion


    // region Search

    override suspend fun searchPages(
        query: String?,
        sort: PropertySort?,
        pagination: Pagination,
    ): ResultPage<Page> {
        return service.searchPages(
            parameters = Triple(query, sort, "page").modelToApi(ApiSearchParametersConverter),
            startCursor = pagination.startCursor
        )
            .apiToModel(ApiPageResultPageConverter)
    }

    override suspend fun searchDatabases(
        query: String?,
        sort: PropertySort?,
        pagination: Pagination,
    ): ResultPage<Database> {
        return service.searchDatabases(
            parameters = Triple(query, sort, "database").modelToApi(ApiSearchParametersConverter),
            startCursor = pagination.startCursor
        )
            .apiToModel(ApiPageResultDatabaseConverter)
    }

    // endregion


    override fun close() = httpClient.close()

    companion object {
        private const val HEADER_NOTION_VERSION = "Notion-Version"
        private const val NOTION_API_VERSION = "2021-05-13"
    }
}

internal expect fun createHttpClient(
    bypassSslChecks: Boolean,
    block: HttpClientConfig<*>.() -> Unit,
): HttpClient


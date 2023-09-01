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
 * and contributors (https://github.com/BoD/klibnotion/graphs/contributors)
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
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import io.ktor.util.encodeBase64
import kotlinx.serialization.json.JsonElement
import org.jraf.klibnotion.internal.api.model.block.ApiAppendBlocksParameters
import org.jraf.klibnotion.internal.api.model.block.ApiBlock
import org.jraf.klibnotion.internal.api.model.database.ApiDatabase
import org.jraf.klibnotion.internal.api.model.database.create.ApiDatabaseCreateParameters
import org.jraf.klibnotion.internal.api.model.database.query.ApiDatabaseQuery
import org.jraf.klibnotion.internal.api.model.database.update.ApiDatabaseUpdateParameters
import org.jraf.klibnotion.internal.api.model.oauth.ApiOAuthGetAccessTokenParameters
import org.jraf.klibnotion.internal.api.model.oauth.ApiOAuthGetAccessTokenResult
import org.jraf.klibnotion.internal.api.model.page.ApiPage
import org.jraf.klibnotion.internal.api.model.page.ApiPageCreateParameters
import org.jraf.klibnotion.internal.api.model.page.ApiPageUpdateParameters
import org.jraf.klibnotion.internal.api.model.pagination.ApiResultPage
import org.jraf.klibnotion.internal.api.model.search.ApiSearchParameters
import org.jraf.klibnotion.internal.api.model.user.ApiUser
import org.jraf.klibnotion.model.base.UuidString

internal class NotionService(private val httpClient: HttpClient) {
    companion object {
        private const val BASE_URL = "https://api.notion.com/v1"

        private const val START_CURSOR = "start_cursor"

        private const val OAUTH = "oauth"
        private const val USERS = "users"
        private const val DATABASES = "databases"
        private const val PAGES = "pages"
        private const val BLOCKS = "blocks"
        private const val SEARCH = "search"

        const val OAUTH_URL_SCHEME = "https"
        const val OAUTH_URL_HOST = "api.notion.com"
        val OAUTH_URL_PATH_SEGMENTS = listOf("v1", OAUTH, "authorize")
    }

    // region OAuth

    @OptIn(InternalAPI::class)
    suspend fun getOAuthAccessToken(
        clientId: String,
        clientSecret: String,
        parameters: ApiOAuthGetAccessTokenParameters,
    ): ApiOAuthGetAccessTokenResult {
        return httpClient.post("$BASE_URL/$OAUTH/token") {
            header(
                HttpHeaders.Authorization,
                getClientSecretBase64(clientId, clientSecret)
            )
            contentType(ContentType.Application.Json)
            setBody(parameters)
        }.body()
    }

    @OptIn(InternalAPI::class)
    private fun getClientSecretBase64(clientId: String, clientSecret: String): String {
        // TODO Don't depend on private encodeBase64 KTOR API
        val clientSecretBase64 = "$clientId:$clientSecret".encodeBase64()
        return "Basic $clientSecretBase64"
    }

    // endregion


    // region Users

    suspend fun getUser(id: String): ApiUser {
        return httpClient.get("$BASE_URL/$USERS/$id").body()
    }


    suspend fun getUserList(startCursor: String?): ApiResultPage<ApiUser> {
        return httpClient.get("$BASE_URL/$USERS") {
            if (startCursor != null) parameter(START_CURSOR, startCursor)
        }.body()
    }

    // endregion


    // region Databases

    suspend fun getDatabase(id: UuidString): ApiDatabase {
        return httpClient.get("$BASE_URL/$DATABASES/$id").body()
    }

    suspend fun getDatabaseList(startCursor: String?): ApiResultPage<ApiDatabase> {
        return httpClient.get("$BASE_URL/$DATABASES") {
            if (startCursor != null) parameter(START_CURSOR, startCursor)
        }.body()
    }

    suspend fun queryDatabase(id: UuidString, query: ApiDatabaseQuery): ApiResultPage<ApiPage> {
        return httpClient.post("$BASE_URL/$DATABASES/$id/query") {
            contentType(ContentType.Application.Json)
            setBody(query)
        }.body()
    }

    suspend fun createDatabase(
        databaseCreate: ApiDatabaseCreateParameters,
    ): ApiDatabase {
        return httpClient.post("$BASE_URL/$DATABASES") {
            contentType(ContentType.Application.Json)
            setBody(databaseCreate)
        }.body()
    }

    suspend fun updateDatabase(
        id: UuidString,
        updateDatabase: ApiDatabaseUpdateParameters,
    ): ApiDatabase {
        return httpClient.patch("$BASE_URL/$DATABASES/$id") {
            contentType(ContentType.Application.Json)
            setBody(updateDatabase)
        }.body()
    }

    // endregion


    // region Pages

    suspend fun getPage(id: UuidString): ApiPage {
        return httpClient.get("$BASE_URL/$PAGES/$id").body()
    }

    suspend fun createPage(parameters: ApiPageCreateParameters): ApiPage {
        return httpClient.post("$BASE_URL/$PAGES") {
            contentType(ContentType.Application.Json)
            setBody(parameters)
        }.body()
    }

    suspend fun updatePage(id: UuidString, parameters: ApiPageUpdateParameters): ApiPage {
        return httpClient.patch("$BASE_URL/$PAGES/$id") {
            contentType(ContentType.Application.Json)
            setBody(parameters)
        }.body()
    }

    suspend fun archivePage(id: UuidString, archive: Boolean): ApiPage {
        return httpClient.patch("$BASE_URL/$PAGES/$id") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("archived" to archive))
        }.body()
    }

    // endregion


    // region Blocks

    suspend fun getBlockList(parentId: UuidString, startCursor: String?): ApiResultPage<ApiBlock> {
        return httpClient.get("$BASE_URL/$BLOCKS/$parentId/children") {
            if (startCursor != null) parameter(START_CURSOR, startCursor)
        }.body()
    }

    suspend fun appendBlockList(parentId: UuidString, parameters: ApiAppendBlocksParameters) {
        httpClient.patch("$BASE_URL/$BLOCKS/$parentId/children") {
            contentType(ContentType.Application.Json)
            setBody(parameters)
        }
    }

    suspend fun getBlock(id: UuidString): ApiBlock {
        return httpClient.get("$BASE_URL/$BLOCKS/$id").body()
    }

    suspend fun updateBlock(id: UuidString, block: JsonElement): ApiBlock {
        return httpClient.patch("$BASE_URL/$BLOCKS/$id") {
            contentType(ContentType.Application.Json)
            setBody(block)
        }.body()
    }

    // endregion


    // region Search

    suspend fun searchPages(parameters: ApiSearchParameters): ApiResultPage<ApiPage> {
        return httpClient.post("$BASE_URL/$SEARCH") {
            contentType(ContentType.Application.Json)
            setBody(parameters)
        }.body()
    }

    suspend fun searchDatabases(parameters: ApiSearchParameters): ApiResultPage<ApiDatabase> {
        return httpClient.post("$BASE_URL/$SEARCH") {
            contentType(ContentType.Application.Json)
            setBody(parameters)
        }.body()
    }

    // endregion

}

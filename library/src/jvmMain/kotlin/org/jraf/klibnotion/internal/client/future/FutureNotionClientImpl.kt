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

package org.jraf.klibnotion.internal.client.future

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.client.future.FutureNotionClient
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.base.reference.DatabaseReference
import org.jraf.klibnotion.model.base.reference.PageReference
import org.jraf.klibnotion.model.block.BlockListProducer
import org.jraf.klibnotion.model.block.MutableBlockList
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.oauth.OAuthCredentials
import org.jraf.klibnotion.model.pagination.Pagination
import org.jraf.klibnotion.model.property.sort.PropertySort
import org.jraf.klibnotion.model.property.value.PropertyValueList
import org.jraf.klibnotion.model.richtext.RichTextList

internal class FutureNotionClientImpl(
    private val notionClient: NotionClient,
) : FutureNotionClient,
    FutureNotionClient.OAuth,
    FutureNotionClient.Users,
    FutureNotionClient.Databases,
    FutureNotionClient.Pages,
    FutureNotionClient.Blocks,
    FutureNotionClient.Search {
    override val oAuth = this
    override val users = this
    override val databases = this
    override val pages = this
    override val blocks = this
    override val search = this

    override fun getUser(id: UuidString) = GlobalScope.future {
        notionClient.users.getUser(id)
    }

    override fun getUserList(pagination: Pagination) = GlobalScope.future {
        notionClient.users.getUserList(pagination)
    }

    override fun getDatabase(id: UuidString) = GlobalScope.future {
        notionClient.databases.getDatabase(id)
    }

    override fun queryDatabase(
        id: UuidString,
        query: DatabaseQuery?,
        sort: PropertySort?,
        pagination: Pagination,
    ) = GlobalScope.future {
        notionClient.databases.queryDatabase(
            id,
            query,
            sort,
            pagination
        )
    }

    override fun getPage(id: UuidString) = GlobalScope.future {
        notionClient.pages.getPage(id)
    }

    override fun createPage(
        parentDatabase: DatabaseReference,
        properties: PropertyValueList,
        content: MutableBlockList?,
    ) = GlobalScope.future {
        notionClient.pages.createPage(parentDatabase, properties, content)
    }

    override fun createPage(
        parentDatabase: DatabaseReference,
        properties: PropertyValueList,
        content: BlockListProducer,
    ) = GlobalScope.future {
        notionClient.pages.createPage(parentDatabase, properties, content)
    }

    override fun createPage(
        parentPage: PageReference,
        title: RichTextList,
        content: MutableBlockList?,
    ) = GlobalScope.future {
        notionClient.pages.createPage(parentPage, title, content)
    }

    override fun createPage(
        parentPage: PageReference,
        title: RichTextList,
        content: BlockListProducer,
    ) = GlobalScope.future {
        notionClient.pages.createPage(parentPage, title, content)
    }

    override fun getUserPromptUri(oAuthCredentials: OAuthCredentials, uniqueState: String) =
        notionClient.oAuth.getUserPromptUri(oAuthCredentials, uniqueState)

    override fun extractCodeAndStateFromRedirectUri(redirectUri: String) =
        notionClient.oAuth.extractCodeAndStateFromRedirectUri(redirectUri)

    override fun getAccessToken(oAuthCredentials: OAuthCredentials, code: String) = GlobalScope.future {
        notionClient.oAuth.getAccessToken(oAuthCredentials, code)
    }

    override fun updatePage(id: UuidString, properties: PropertyValueList) = GlobalScope.future {
        notionClient.pages.updatePage(id, properties)
    }

    override fun getBlockList(parentId: UuidString, pagination: Pagination) = GlobalScope.future {
        notionClient.blocks.getBlockList(parentId, pagination)
    }

    override fun getAllBlockListRecursively(parentId: UuidString) = GlobalScope.future {
        notionClient.blocks.getAllBlockListRecursively(parentId)
    }

    override fun appendBlockList(parentId: UuidString, blocks: MutableBlockList) = GlobalScope.future<Void?> {
        notionClient.blocks.appendBlockList(parentId, blocks)
        null
    }

    override fun appendBlockList(parentId: UuidString, blocks: BlockListProducer) = GlobalScope.future<Void?> {
        notionClient.blocks.appendBlockList(parentId, blocks)
        null
    }

    override fun searchPages(query: String?, sort: PropertySort?, pagination: Pagination) = GlobalScope.future {
        notionClient.search.searchPages(query, sort, pagination)
    }

    override fun searchDatabases(query: String?, sort: PropertySort?, pagination: Pagination) = GlobalScope.future {
        notionClient.search.searchDatabases(query, sort, pagination)
    }

    override fun close() = notionClient.close()

}
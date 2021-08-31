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

package org.jraf.klibnotion.internal.client.blocking

import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.client.blocking.BlockingNotionClient
import org.jraf.klibnotion.internal.runBlocking
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.base.reference.DatabaseReference
import org.jraf.klibnotion.model.base.reference.PageReference
import org.jraf.klibnotion.model.block.BlockListProducer
import org.jraf.klibnotion.model.block.MutableBlockList
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.oauth.OAuthCredentials
import org.jraf.klibnotion.model.pagination.Pagination
import org.jraf.klibnotion.model.property.sort.PropertySort
import org.jraf.klibnotion.model.property.spec.PropertySpecList
import org.jraf.klibnotion.model.property.value.PropertyValueList
import org.jraf.klibnotion.model.richtext.RichTextList

internal class BlockingNotionClientImpl(
    private val notionClient: NotionClient,
) : BlockingNotionClient,
    BlockingNotionClient.OAuth,
    BlockingNotionClient.Users,
    BlockingNotionClient.Databases,
    BlockingNotionClient.Pages,
    BlockingNotionClient.Blocks,
    BlockingNotionClient.Search {
    override val oAuth = this
    override val users = this
    override val databases = this
    override val pages = this
    override val blocks = this
    override val search = this

    override fun getUserPromptUri(oAuthCredentials: OAuthCredentials, uniqueState: String) =
        notionClient.oAuth.getUserPromptUri(oAuthCredentials, uniqueState)

    override fun extractCodeAndStateFromRedirectUri(redirectUri: String) =
        notionClient.oAuth.extractCodeAndStateFromRedirectUri(redirectUri)

    override fun getAccessToken(oAuthCredentials: OAuthCredentials, code: String) = runBlocking {
        notionClient.oAuth.getAccessToken(oAuthCredentials, code)
    }

    override fun getUser(id: UuidString) = runBlocking {
        notionClient.users.getUser(id)
    }

    override fun getUserList(pagination: Pagination) = runBlocking {
        notionClient.users.getUserList(pagination)
    }

    override fun getDatabase(id: UuidString) = runBlocking {
        notionClient.databases.getDatabase(id)
    }

    override fun getDatabaseList(pagination: Pagination) = runBlocking {
        notionClient.databases.getDatabaseList(pagination)
    }

    override fun queryDatabase(
        id: UuidString,
        query: DatabaseQuery?,
        sort: PropertySort?,
        pagination: Pagination,
    ) = runBlocking {
        notionClient.databases.queryDatabase(
            id,
            query,
            sort,
            pagination
        )
    }

    override fun createDatabase(
        parentPageId: UuidString,
        title: RichTextList,
        properties: PropertySpecList,
    ) = runBlocking {
        notionClient.databases.createDatabase(
            parentPageId,
            title,
            properties
        )
    }

    override fun getPage(id: UuidString) = runBlocking {
        notionClient.pages.getPage(id)
    }

    override fun createPage(
        parentDatabase: DatabaseReference,
        properties: PropertyValueList,
        content: MutableBlockList?,
    ) = runBlocking {
        notionClient.pages.createPage(parentDatabase, properties, content)
    }

    override fun createPage(
        parentDatabase: DatabaseReference,
        properties: PropertyValueList,
        content: BlockListProducer,
    ) = runBlocking {
        notionClient.pages.createPage(parentDatabase, properties, content)
    }

    override fun createPage(
        parentPage: PageReference,
        title: RichTextList,
        content: MutableBlockList?,
    ) = runBlocking {
        notionClient.pages.createPage(parentPage, title, content)
    }

    override fun createPage(
        parentPage: PageReference,
        title: RichTextList,
        content: BlockListProducer,
    ) = runBlocking {
        notionClient.pages.createPage(parentPage, title, content)
    }

    override fun updatePage(id: UuidString, properties: PropertyValueList) = runBlocking {
        notionClient.pages.updatePage(id, properties)
    }

    override fun setPageArchived(id: UuidString, archived: Boolean) = runBlocking {
        notionClient.pages.setPageArchived(id, archived)
    }

    override fun getBlockList(parentId: UuidString, pagination: Pagination) = runBlocking {
        notionClient.blocks.getBlockList(parentId, pagination)
    }

    override fun getAllBlockListRecursively(parentId: UuidString) = runBlocking {
        notionClient.blocks.getAllBlockListRecursively(parentId)
    }

    override fun appendBlockList(parentId: UuidString, blocks: MutableBlockList) = runBlocking {
        notionClient.blocks.appendBlockList(parentId, blocks)
    }

    override fun appendBlockList(parentId: UuidString, blocks: BlockListProducer) = runBlocking {
        notionClient.blocks.appendBlockList(parentId, blocks)
    }

    override fun getBlock(id: UuidString, retrieveChildrenRecursively: Boolean) = runBlocking {
        notionClient.blocks.getBlock(id, retrieveChildrenRecursively)
    }

    override fun searchPages(query: String?, sort: PropertySort?, pagination: Pagination) = runBlocking {
        notionClient.search.searchPages(query, sort, pagination)
    }

    override fun searchDatabases(query: String?, sort: PropertySort?, pagination: Pagination) = runBlocking {
        notionClient.search.searchDatabases(query, sort, pagination)
    }

    override fun close() = notionClient.close()
}
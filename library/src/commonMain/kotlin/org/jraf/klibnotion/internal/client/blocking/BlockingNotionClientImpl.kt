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
import org.jraf.klibnotion.model.block.BlockListProducer
import org.jraf.klibnotion.model.block.MutableBlockList
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.database.query.DatabaseQuerySort
import org.jraf.klibnotion.model.pagination.Pagination
import org.jraf.klibnotion.model.property.value.PropertyValueList

internal class BlockingNotionClientImpl(
    private val notionClient: NotionClient,
) : BlockingNotionClient,
    BlockingNotionClient.Users,
    BlockingNotionClient.Databases,
    BlockingNotionClient.Pages,
    BlockingNotionClient.Blocks {
    override val users = this
    override val databases = this
    override val pages = this
    override val blocks = this

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
        sort: DatabaseQuerySort?,
        pagination: Pagination,
    ) = runBlocking {
        notionClient.databases.queryDatabase(
            id,
            query,
            sort,
            pagination
        )
    }

    override fun getPage(id: UuidString) = runBlocking {
        notionClient.pages.getPage(id)
    }

    override fun createPage(
        parentDatabaseId: UuidString,
        properties: PropertyValueList,
        content: MutableBlockList?,
    ) = runBlocking {
        notionClient.pages.createPage(parentDatabaseId, properties, content)
    }

    override fun createPage(
        parentDatabaseId: UuidString,
        properties: PropertyValueList,
        content: BlockListProducer,
    ) = runBlocking {
        notionClient.pages.createPage(parentDatabaseId, properties, content)
    }

    override fun updatePage(id: UuidString, properties: PropertyValueList) = runBlocking {
        notionClient.pages.updatePage(id, properties)
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

    override fun close() = notionClient.close()
}
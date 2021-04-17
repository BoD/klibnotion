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
import org.jraf.klibnotion.model.block.BlockListProducer
import org.jraf.klibnotion.model.block.MutableBlockList
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.database.query.DatabaseQuerySort
import org.jraf.klibnotion.model.pagination.Pagination
import org.jraf.klibnotion.model.property.value.PropertyValueList

internal class FutureNotionClientImpl(
    private val notionClient: NotionClient,
) : FutureNotionClient,
    FutureNotionClient.Users,
    FutureNotionClient.Databases,
    FutureNotionClient.Pages {
    override val users = this
    override val databases = this
    override val pages = this

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
        sort: DatabaseQuerySort?,
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
        parentDatabaseId: UuidString,
        properties: PropertyValueList,
        content: MutableBlockList?,
    ) = GlobalScope.future {
        notionClient.pages.createPage(parentDatabaseId, properties, content)
    }

    override fun createPage(
        parentDatabaseId: UuidString,
        properties: PropertyValueList,
        content: BlockListProducer,
    ) = GlobalScope.future {
        notionClient.pages.createPage(parentDatabaseId, properties, content)
    }

    override fun updatePage(id: UuidString, properties: PropertyValueList) = GlobalScope.future {
        notionClient.pages.updatePage(id, properties)
    }

    override fun close() = notionClient.close()

}
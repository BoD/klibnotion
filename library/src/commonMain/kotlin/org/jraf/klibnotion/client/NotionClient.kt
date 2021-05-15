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

package org.jraf.klibnotion.client

import org.jraf.klibnotion.internal.client.NotionClientImpl
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.base.reference.DatabaseReference
import org.jraf.klibnotion.model.base.reference.PageReference
import org.jraf.klibnotion.model.block.Block
import org.jraf.klibnotion.model.block.BlockListProducer
import org.jraf.klibnotion.model.block.MutableBlockList
import org.jraf.klibnotion.model.database.Database
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.database.query.DatabaseQuerySort
import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.pagination.Pagination
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.value.PropertyValueList
import org.jraf.klibnotion.model.richtext.RichTextList
import org.jraf.klibnotion.model.user.User
import kotlin.jvm.JvmStatic

interface NotionClient {
    companion object {
        @JvmStatic
        fun newInstance(configuration: ClientConfiguration): NotionClient =
            NotionClientImpl(configuration)
    }

    /**
     * User related APIs.
     */
    interface Users {
        /**
         * Retrieve a user.
         * @see <a href="https://www.notion.so/Retrieve-a-user-94e3370083a74d96b8091ae28cff6058">Retrieve a user</a>
         */
        suspend fun getUser(id: UuidString): User

        /**
         * List all users.
         * @see <a href="https://www.notion.so/List-all-users-5d5fc45e1a5144e0abcbc9b8ac15206d">List all users</a>
         */
        suspend fun getUserList(pagination: Pagination = Pagination()): ResultPage<User>
    }

    /**
     * Database related APIs.
     */
    interface Databases {
        /**
         * Retrieve a database.
         * @see <a href="https://www.notion.so/Retrieve-a-database-54cdc49884544e1382a27abbfca24b6e">Retrieve a database</a>
         */
        suspend fun getDatabase(id: UuidString): Database

        /**
         * List Databases.
         * This lists all the databases that have been shared with your bot.
         * @see <a href="https://www.notion.so/notiondevs/List-Databases-0670fd2cbc464d6e9b17f8ac80c0794d">List Databases</a>
         */
        suspend fun getDatabaseList(pagination: Pagination = Pagination()): ResultPage<Database>

        /**
         * Query a database.
         * @see <a href="https://www.notion.so/Query-a-database-f150c8e10ead4afe9045d56bc8114855">Query a database</a>
         */
        suspend fun queryDatabase(
            id: UuidString,
            query: DatabaseQuery? = null,
            sort: DatabaseQuerySort? = null,
            pagination: Pagination = Pagination(),
        ): ResultPage<Page>
    }

    /**
     * Page related APIs.
     */
    interface Pages {
        /**
         * Retrieve a page.
         * @see <a href="https://www.notion.so/Retrieve-a-page-dabcf81142514685a70b38c911e6d126">Retrieve a page</a>
         */
        suspend fun getPage(id: UuidString): Page

        /**
         * Create a page in a database.
         * @see <a href="https://www.notion.so/Create-a-page-9bd15f8d8082429b82dbe6c4ea88413b">Create a page</a>
         */
        suspend fun createPage(
            parentDatabase: DatabaseReference,
            properties: PropertyValueList = PropertyValueList(),
            content: MutableBlockList? = null,
        ): Page

        /**
         * Create a page in a database.
         * @see <a href="https://www.notion.so/Create-a-page-9bd15f8d8082429b82dbe6c4ea88413b">Create a page</a>
         */
        suspend fun createPage(
            parentDatabase: DatabaseReference,
            properties: PropertyValueList = PropertyValueList(),
            content: BlockListProducer,
        ): Page

        /**
         * Create a page in a page.
         * @see <a href="https://www.notion.so/Create-a-page-9bd15f8d8082429b82dbe6c4ea88413b">Create a page</a>
         */
        suspend fun createPage(
            parentPage: PageReference,
            title: RichTextList = RichTextList(),
            content: MutableBlockList? = null,
        ): Page

        /**
         * Create a page in a page.
         * @see <a href="https://www.notion.so/Create-a-page-9bd15f8d8082429b82dbe6c4ea88413b">Create a page</a>
         */
        suspend fun createPage(
            parentPage: PageReference,
            title: RichTextList = RichTextList(),
            content: BlockListProducer,
        ): Page

        /**
         * Update a page.
         * @see <a href="https://www.notion.so/Update-page-properties-70ef58bea0034d21b5fec686cb5bf980">Update page properties</a>
         */
        suspend fun updatePage(id: UuidString, properties: PropertyValueList): Page
    }

    /**
     * Blocks (content) related APIs.
     */
    interface Blocks {
        /**
         * Retrieve children blocks of the specified object.
         *
         * Note: this will *not* retrieve the children blocks (if any). Blocks that don't have children will have their
         * [Block.children] property set to `null`, whereas blocks that do have children will have it set to an empty list.
         *
         * If you need to retrieve the children blocks recursively refer to the [getAllBlockListRecursively] method instead.
         *
         * @see <a href="https://www.notion.so/Retrieve-block-children-7ad593137c6348e7be1e37a42ef29027">Retrieve block children</a>
         */
        suspend fun getBlockList(parentId: UuidString, pagination: Pagination = Pagination()): ResultPage<Block>

        /**
         * Retrieve all children blocks (including all the pages) of the specified object, and including all their children,
         * recursively.
         *
         * **Caution:** be aware that this will potentially make many network calls (depending on the size of the block list you
         * are retrieving).
         */
        suspend fun getAllBlockListRecursively(parentId: UuidString): List<Block>

        /**
         * Append blocks to the children of the specified object.
         * @see <a href="https://www.notion.so/Append-block-children-6d9f2aa8efc14b06a8ddfae75364e74f">Append block children</a>
         */
        suspend fun appendBlockList(parentId: UuidString, blocks: MutableBlockList)

        /**
         * Append blocks to the children of the specified object.
         * @see <a href="https://www.notion.so/Append-block-children-6d9f2aa8efc14b06a8ddfae75364e74f">Append block children</a>
         */
        suspend fun appendBlockList(parentId: UuidString, blocks: BlockListProducer)
    }


    /**
     * User related APIs.
     */
    val users: Users

    /**
     * Database related APIs.
     */
    val databases: Databases

    /**
     * Page related APIs.
     */
    val pages: Pages

    /**
     * Page related APIs.
     */
    val blocks: Blocks


    /**
     * Dispose of this client instance.
     * This will release some resources so it is recommended to call it after use.
     *
     * **Note: this client will no longer be usable after this is called.**
     */
    fun close()
}

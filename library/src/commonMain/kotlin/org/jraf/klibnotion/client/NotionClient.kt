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
import org.jraf.klibnotion.model.database.Database
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.database.query.DatabaseQuerySort
import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.pagination.Pagination
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.value.PropertyValueList
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
        suspend fun getPage(id: UuidString, isArchived: Boolean = false): Page

        /**
         * Create a page.
         * @see <a href="https://www.notion.so/Create-a-page-9bd15f8d8082429b82dbe6c4ea88413b">Create a page</a>
         */
        suspend fun createPage(parentDatabaseId: UuidString, properties: PropertyValueList): Page
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
     * Dispose of this client instance.
     * This will release some resources so it is recommended to call it after use.
     *
     * **Note: this client will no longer be usable after this is called.**
     */
    fun close()
}

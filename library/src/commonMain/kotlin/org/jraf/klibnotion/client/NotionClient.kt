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
import kotlin.jvm.JvmStatic

interface NotionClient {
    companion object {
        @JvmStatic
        fun newInstance(configuration: ClientConfiguration): NotionClient =
            NotionClientImpl(configuration)
    }

    /**
     * OAuth related APIs.
     */
    interface OAuth {
        /**
         * Get the URL used to prompt users to add an integration.
         * @see <a href="https://developers.notion.com/docs/authorization#prompting-users-to-add-an-integration">Prompting users to add an integration</a>
         */
        fun getUserPromptUri(
            oAuthCredentials: OAuthCredentials,
            uniqueState: String,
        ): String

        /**
         * Extract the code and state from the URI that the user was redirected to after login.
         * @see <a href="https://developers.notion.com/docs/authorization#exchanging-the-grant-for-an-access-token">Exchanging the grant for an access token</a>
         *
         * @return The code and state, or `null` if extraction failed.
         */
        fun extractCodeAndStateFromRedirectUri(redirectUri: String): OAuthCodeAndState?

        /**
         * Retrieve the OAuth access token from a code obtained via [getUserPromptUri] and [extractCodeAndStateFromRedirectUri].
         * @see <a href="https://developers.notion.com/docs/authorization#exchanging-the-grant-for-an-access-token">Exchanging the grant for an access token</a>
         */
        suspend fun getAccessToken(oAuthCredentials: OAuthCredentials, code: String): OAuthGetAccessTokenResult
    }


    /**
     * User related APIs.
     */
    interface Users {
        /**
         * Retrieve a user.
         * @see <a href="https://developers.notion.com/reference/get-user">Retrieve a user</a>
         */
        suspend fun getUser(id: UuidString): User

        /**
         * List all users.
         * @see <a href="https://developers.notion.com/reference/get-users">List all users</a>
         */
        suspend fun getUserList(pagination: Pagination = Pagination()): ResultPage<User>
    }

    /**
     * Database related APIs.
     */
    interface Databases {
        /**
         * Retrieve a database.
         * @see <a href="https://developers.notion.com/reference/get-database">Retrieve a database</a>
         */
        suspend fun getDatabase(id: UuidString): Database

        /**
         * List Databases.
         * This lists all the databases that have been shared with your bot.
         * @see <a href="https://developers.notion.com/reference/get-databases">List Databases</a>
         */
        suspend fun getDatabaseList(pagination: Pagination = Pagination()): ResultPage<Database>

        /**
         * Query a database.
         * @see <a href="https://developers.notion.com/reference/post-database-query">Query a database</a>
         */
        suspend fun queryDatabase(
            id: UuidString,
            query: DatabaseQuery? = null,
            sort: PropertySort? = null,
            pagination: Pagination = Pagination(),
        ): ResultPage<Page>

        /**
         * Create a database inside a page.
         *
         * Note: [properties] must contain exactly one title property.
         * @see <a href="https://developers.notion.com/reference/create-a-database">Create a database</a>
         */
        suspend fun createDatabase(
            parentPageId: UuidString,
            title: RichTextList = RichTextList(),
            properties: PropertySpecList = PropertySpecList(),
        ): Database
    }

    /**
     * Page related APIs.
     */
    interface Pages {
        /**
         * Retrieve a page.
         * @see <a href="https://developers.notion.com/reference/get-page">Retrieve a page</a>
         */
        suspend fun getPage(id: UuidString): Page

        /**
         * Create a page in a database.
         * @see <a href="https://developers.notion.com/reference/post-page">Create a page</a>
         */
        suspend fun createPage(
            parentDatabase: DatabaseReference,
            properties: PropertyValueList = PropertyValueList(),
            content: MutableBlockList? = null,
        ): Page

        /**
         * Create a page in a database.
         * @see <a href="https://developers.notion.com/reference/post-page">Create a page</a>
         */
        suspend fun createPage(
            parentDatabase: DatabaseReference,
            properties: PropertyValueList = PropertyValueList(),
            content: BlockListProducer,
        ): Page

        /**
         * Create a page in a page.
         * @see <a href="https://developers.notion.com/reference/post-page">Create a page</a>
         */
        suspend fun createPage(
            parentPage: PageReference,
            title: RichTextList = RichTextList(),
            content: MutableBlockList? = null,
        ): Page

        /**
         * Create a page in a page.
         * @see <a href="https://developers.notion.com/reference/post-page">Create a page</a>
         */
        suspend fun createPage(
            parentPage: PageReference,
            title: RichTextList = RichTextList(),
            content: BlockListProducer,
        ): Page

        /**
         * Update a page.
         * @see <a href="https://developers.notion.com/reference/patch-page">Update page properties</a>
         */
        suspend fun updatePage(id: UuidString, properties: PropertyValueList): Page

        /**
         * Mark the page as archived or not.
         * @see <a href="https://developers.notion.com/reference/patch-page#archive-delete-a-page">Archive a page</a>
         */
        suspend fun setPageArchived(id: UuidString, archived: Boolean): Page
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
         * @see <a href="https://developers.notion.com/reference/get-block-children">Retrieve block children</a>
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
         * @see <a href="https://developers.notion.com/reference/patch-block-children">Append block children</a>
         */
        suspend fun appendBlockList(parentId: UuidString, blocks: MutableBlockList)

        /**
         * Append blocks to the children of the specified object.
         * @see <a href="https://developers.notion.com/reference/patch-block-children">Append block children</a>
         */
        suspend fun appendBlockList(parentId: UuidString, blocks: BlockListProducer)

        /**
         * Retrieve a block.
         *
         * Note:
         * - when [retrieveChildrenRecursively] is set to `false`, this will *not* retrieve the children blocks (if any).
         * Blocks that don't have children will have their [Block.children] property set to `null`,
         * whereas blocks that do have children will have it set to an empty list.
         *- when [retrieveChildrenRecursively] is set to `true`, all the block's children, will be retrieved, recursively.
         * **Caution:** be aware that this will potentially make many network calls (depending on the size of the children list you
         * are retrieving).
         *
         * @see <a href="https://developers.notion.com/reference/retrieve-a-block">Retrieve a block</a>
         */
        suspend fun getBlock(id: UuidString, retrieveChildrenRecursively: Boolean = false): Block
    }


    /**
     * Search related APIs.
     */
    interface Search {
        /**
         * Search pages.
         *
         * The [query] is optional, when `null` this will return all pages.
         * @see <a href="https://developers.notion.com/reference/post-search">Search</a>
         */
        suspend fun searchPages(
            query: String? = null,
            sort: PropertySort? = null,
            pagination: Pagination = Pagination(),
        ): ResultPage<Page>

        /**
         * Search databases.
         *
         * The [query] is optional, when `null` this will return all databases.
         * @see <a href="https://developers.notion.com/reference/post-search">Search</a>
         */
        suspend fun searchDatabases(
            query: String? = null,
            sort: PropertySort? = null,
            pagination: Pagination = Pagination(),
        ): ResultPage<Database>
    }

    /**
     * OAuth related APIs.
     */
    val oAuth: OAuth

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
     * Block related APIs.
     */
    val blocks: Blocks

    /**
     * Search related APIs.
     */
    val search: Search

    /**
     * Dispose of this client instance.
     * This will release some resources so it is recommended to call it after use.
     *
     * **Note: this client will no longer be usable after this is called.**
     */
    fun close()
}

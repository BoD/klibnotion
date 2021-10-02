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

@file:JvmName("FutureNotionClientUtils")

package org.jraf.klibnotion.client.future

import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.internal.client.future.FutureNotionClientImpl
import org.jraf.klibnotion.model.base.EmojiOrFile
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.base.reference.DatabaseReference
import org.jraf.klibnotion.model.base.reference.PageReference
import org.jraf.klibnotion.model.block.Block
import org.jraf.klibnotion.model.block.BlockListProducer
import org.jraf.klibnotion.model.block.MutableBlockList
import org.jraf.klibnotion.model.database.Database
import org.jraf.klibnotion.model.database.query.DatabaseQuery
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
import java.util.concurrent.Future

/**
 * A [Future] based version of a Notion client.
 *
 * All the methods here are non blocking and return their results as a [Future].
 *
 * This is useful from Java, which doesn't have a notion of `suspend` functions.
 */
interface FutureNotionClient {
    /**
     * See [NotionClient.OAuth].
     */
    interface OAuth {
        /**
         * See [NotionClient.OAuth.getUserPromptUri].
         */
        fun getUserPromptUri(
            oAuthCredentials: OAuthCredentials,
            uniqueState: String,
        ): String

        /**
         * See [NotionClient.OAuth.extractCodeAndStateFromRedirectUri].
         */
        fun extractCodeAndStateFromRedirectUri(redirectUri: String): OAuthCodeAndState?

        /**
         * See [NotionClient.OAuth.getAccessToken].
         */
        fun getAccessToken(oAuthCredentials: OAuthCredentials, code: String): Future<OAuthGetAccessTokenResult>
    }

    /**
     * See [NotionClient.Users].
     */
    interface Users {
        /**
         * See [NotionClient.Users.getUser].
         */
        fun getUser(id: UuidString): Future<User>

        /**
         * See [NotionClient.Users.getUserList].
         */
        fun getUserList(pagination: Pagination = Pagination()): Future<ResultPage<User>>
    }

    /**
     * See [NotionClient.Databases].
     */
    interface Databases {
        /**
         * See [NotionClient.Databases.getDatabase].
         */
        fun getDatabase(id: UuidString): Future<Database>

        /**
         * See [NotionClient.Databases.getDatabaseList].
         */
        fun getDatabaseList(pagination: Pagination = Pagination()): Future<ResultPage<Database>>

        /**
         * See [NotionClient.Databases.queryDatabase].
         */
        fun queryDatabase(
            id: UuidString,
            query: DatabaseQuery? = null,
            sort: PropertySort? = null,
            pagination: Pagination = Pagination(),
        ): Future<ResultPage<Page>>

        /**
         * See [NotionClient.Databases.createDatabase].
         */
        fun createDatabase(
            parentPageId: UuidString,
            title: RichTextList = RichTextList(),
            properties: PropertySpecList = PropertySpecList(),
        ): Future<Database>

        /**
         * See [NotionClient.Databases.updateDatabase].
         */
        fun updateDatabase(
            id: UuidString,
            title: RichTextList? = null,
            properties: PropertySpecList? = null,
        ): Future<Database>
    }

    /**
     * See [NotionClient.Pages].
     */
    interface Pages {
        /**
         * See [NotionClient.Pages.getPage].
         */
        fun getPage(id: UuidString): Future<Page>

        /**
         * See [NotionClient.Pages.createPage].
         */
        fun createPage(
            parentDatabase: DatabaseReference,
            icon: EmojiOrFile? = null,
            cover: File? = null,
            properties: PropertyValueList = PropertyValueList(),
            content: MutableBlockList? = null,
        ): Future<Page>

        /**
         * See [NotionClient.Pages.createPage].
         */
        fun createPage(
            parentDatabase: DatabaseReference,
            icon: EmojiOrFile? = null,
            cover: File? = null,
            properties: PropertyValueList = PropertyValueList(),
            content: BlockListProducer,
        ): Future<Page>

        /**
         * See [NotionClient.Pages.createPage].
         */
        fun createPage(
            parentPage: PageReference,
            title: RichTextList = RichTextList(),
            icon: EmojiOrFile? = null,
            cover: File? = null,
            content: MutableBlockList? = null,
        ): Future<Page>

        /**
         * See [NotionClient.Pages.createPage].
         */
        fun createPage(
            parentPage: PageReference,
            title: RichTextList = RichTextList(),
            icon: EmojiOrFile? = null,
            cover: File? = null,
            content: BlockListProducer,
        ): Future<Page>

        /**
         * See [NotionClient.Pages.updatePage].
         */
        fun updatePage(id: UuidString, properties: PropertyValueList): Future<Page>

        /**
         * See [NotionClient.Pages.setPageArchived].
         */
        fun setPageArchived(id: UuidString, archived: Boolean): Future<Page>
    }

    /**
     * See [NotionClient.Blocks].
     */
    interface Blocks {
        /**
         * See [NotionClient.Blocks.getBlockList].
         */
        fun getBlockList(parentId: UuidString, pagination: Pagination = Pagination()): Future<ResultPage<Block>>

        /**
         * See [NotionClient.Blocks.getAllBlockListRecursively].
         */
        fun getAllBlockListRecursively(parentId: UuidString): Future<List<Block>>

        /**
         * See [NotionClient.Blocks.appendBlockList].
         */
        fun appendBlockList(parentId: UuidString, blocks: MutableBlockList): Future<Void?>

        /**
         * See [NotionClient.Blocks.appendBlockList].
         */
        fun appendBlockList(parentId: UuidString, blocks: BlockListProducer): Future<Void?>

        /**
         * See [NotionClient.Blocks.getBlock].
         */
        fun getBlock(id: UuidString, retrieveChildrenRecursively: Boolean): Future<Block>

        /**
         * See [NotionClient.Blocks.updateBlock].
         */
        fun updateBlock(id: UuidString, block: Block): Future<Block>
    }


    /**
     * See [NotionClient.Search].
     */
    interface Search {
        /**
         * See [NotionClient.Search.searchPages].
         */
        fun searchPages(
            query: String? = null,
            sort: PropertySort? = null,
            pagination: Pagination = Pagination(),
        ): Future<ResultPage<Page>>

        /**
         * See [NotionClient.Search.searchDatabases].
         */
        fun searchDatabases(
            query: String? = null,
            sort: PropertySort? = null,
            pagination: Pagination = Pagination(),
        ): Future<ResultPage<Database>>
    }


    /**
     * See [NotionClient.oAuth].
     */
    val oAuth: OAuth

    /**
     * See [NotionClient.users].
     */
    val users: Users

    /**
     * See [NotionClient.databases].
     */
    val databases: Databases

    /**
     * See [NotionClient.pages].
     */
    val pages: Pages

    /**
     * See [NotionClient.blocks].
     */
    val blocks: Blocks

    /**
     * See [NotionClient.search].
     */
    val search: Search


    /**
     * See [NotionClient.close].
     */
    fun close()
}

/**
 * Get a Future based client from a [NotionClient].
 *
 * This is useful from Java, which doesn't have a notion of `suspend` functions.
 */
fun NotionClient.asFutureNotionClient(): FutureNotionClient {
    return FutureNotionClientImpl(this)
}
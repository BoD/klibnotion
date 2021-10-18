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

package org.jraf.klibnotion.internal.api.model.block

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.apiToModel
import org.jraf.klibnotion.internal.api.model.base.ApiEmojiOrFileConverter
import org.jraf.klibnotion.internal.api.model.date.ApiDateStringConverter
import org.jraf.klibnotion.internal.api.model.richtext.ApiRichTextConverter
import org.jraf.klibnotion.internal.model.block.BookmarkBlockImpl
import org.jraf.klibnotion.internal.model.block.BulletedListItemBlockImpl
import org.jraf.klibnotion.internal.model.block.CalloutBlockImpl
import org.jraf.klibnotion.internal.model.block.ChildDatabaseBlockImpl
import org.jraf.klibnotion.internal.model.block.ChildPageBlockImpl
import org.jraf.klibnotion.internal.model.block.CodeBlockImpl
import org.jraf.klibnotion.internal.model.block.DividerBlockImpl
import org.jraf.klibnotion.internal.model.block.EmbedBlockImpl
import org.jraf.klibnotion.internal.model.block.EquationBlockImpl
import org.jraf.klibnotion.internal.model.block.Heading1BlockImpl
import org.jraf.klibnotion.internal.model.block.Heading2BlockImpl
import org.jraf.klibnotion.internal.model.block.Heading3BlockImpl
import org.jraf.klibnotion.internal.model.block.NumberedListItemBlockImpl
import org.jraf.klibnotion.internal.model.block.ParagraphBlockImpl
import org.jraf.klibnotion.internal.model.block.QuoteBlockImpl
import org.jraf.klibnotion.internal.model.block.TableOfContentsBlockImpl
import org.jraf.klibnotion.internal.model.block.ToDoBlockImpl
import org.jraf.klibnotion.internal.model.block.ToggleBlockImpl
import org.jraf.klibnotion.internal.model.block.UnknownTypeBlockImpl
import org.jraf.klibnotion.model.block.Block
import org.jraf.klibnotion.model.richtext.RichTextList

internal object ApiInBlockConverter : ApiConverter<ApiBlock, Block>() {
    override fun apiToModel(apiModel: ApiBlock): Block {
        val id = apiModel.id
        val created = apiModel.created_time.apiToModel(ApiDateStringConverter).timestamp
        val lastEdited = apiModel.last_edited_time.apiToModel(ApiDateStringConverter).timestamp
        // HACK: we use empty list as a signal that there are children that need fetching
        val children: List<Block>? = if (apiModel.has_children) emptyList() else null
        return when (val type = apiModel.type) {
            "paragraph" -> {
                ParagraphBlockImpl(
                    id = id,
                    created = created,
                    lastEdited = lastEdited,
                    text = apiModel.paragraph.toRichTextList(),
                    children = children
                )
            }

            "heading_1" -> Heading1BlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                text = apiModel.heading_1.toRichTextList()
            )

            "heading_2" -> Heading2BlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                text = apiModel.heading_2.toRichTextList()
            )

            "heading_3" -> Heading3BlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                text = apiModel.heading_3.toRichTextList()
            )

            "bulleted_list_item" -> BulletedListItemBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                text = apiModel.bulleted_list_item.toRichTextList(),
                children = children
            )

            "numbered_list_item" -> NumberedListItemBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                text = apiModel.numbered_list_item.toRichTextList(),
                children = children
            )

            "to_do" -> ToDoBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                text = apiModel.to_do.toRichTextList(),
                checked = apiModel.to_do!!.checked,
                children = children
            )

            "toggle" -> ToggleBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                text = apiModel.toggle.toRichTextList(),
                children = children
            )

            "child_page" -> ChildPageBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                title = apiModel.child_page!!.title
            )

            "child_database" -> ChildDatabaseBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                title = apiModel.child_database!!.title
            )

            "code" -> CodeBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                language = apiModel.code!!.language,
                text = apiModel.code.toRichTextList()
            )

            "equation" -> EquationBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                expression = apiModel.equation!!.expression
            )

            "callout" -> CalloutBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                text = apiModel.callout!!.toRichTextList(),
                children = children,
                icon = apiModel.callout.icon.apiToModel(ApiEmojiOrFileConverter)
            )

            "quote" -> QuoteBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                children = children,
                text = apiModel.quote!!.toRichTextList()
            )

            "embed" -> EmbedBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                url = apiModel.embed!!.url
            )

            "bookmark" -> BookmarkBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                url = apiModel.bookmark!!.url,
                caption = apiModel.bookmark.toRichTextList()
            )

            "divider" -> DividerBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
            )

            "table_of_contents" -> TableOfContentsBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
            )

            else -> UnknownTypeBlockImpl(
                id = id,
                created = created,
                lastEdited = lastEdited,
                type = type,
            )
        }
    }

    private fun ApiBlockText?.toRichTextList() = RichTextList(this!!.text.apiToModel(ApiRichTextConverter))
    private fun ApiBlockTodo?.toRichTextList() = RichTextList(this!!.text.apiToModel(ApiRichTextConverter))
    private fun ApiBlockCode?.toRichTextList() = RichTextList(this!!.text.apiToModel(ApiRichTextConverter))
    private fun ApiBlockCallout?.toRichTextList() = RichTextList(this!!.text.apiToModel(ApiRichTextConverter))
    private fun ApiBlockBookmark?.toRichTextList() = RichTextList(this!!.caption.apiToModel(ApiRichTextConverter))
}

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

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.base.ApiOutEmojiOrFileConverter
import org.jraf.klibnotion.internal.api.model.modelToApi
import org.jraf.klibnotion.internal.api.model.richtext.ApiOutRichTextListConverter
import org.jraf.klibnotion.model.block.Block
import org.jraf.klibnotion.model.block.BookmarkBlock
import org.jraf.klibnotion.model.block.BulletedListItemBlock
import org.jraf.klibnotion.model.block.CalloutBlock
import org.jraf.klibnotion.model.block.CodeBlock
import org.jraf.klibnotion.model.block.DividerBlock
import org.jraf.klibnotion.model.block.EmbedBlock
import org.jraf.klibnotion.model.block.EquationBlock
import org.jraf.klibnotion.model.block.Heading1Block
import org.jraf.klibnotion.model.block.Heading2Block
import org.jraf.klibnotion.model.block.Heading3Block
import org.jraf.klibnotion.model.block.NumberedListItemBlock
import org.jraf.klibnotion.model.block.ParagraphBlock
import org.jraf.klibnotion.model.block.QuoteBlock
import org.jraf.klibnotion.model.block.TableOfContentsBlock
import org.jraf.klibnotion.model.block.ToDoBlock
import org.jraf.klibnotion.model.block.ToggleBlock
import org.jraf.klibnotion.model.richtext.RichTextList

internal object ApiOutBlockConverter : ApiConverter<JsonElement, Block>() {

    override fun modelToApi(model: Block): JsonElement {
        return buildJsonObject {
            put("object", "block")

            val type = when (model) {
                is ParagraphBlock -> "paragraph"
                is Heading1Block -> "heading_1"
                is Heading2Block -> "heading_2"
                is Heading3Block -> "heading_3"
                is BulletedListItemBlock -> "bulleted_list_item"
                is NumberedListItemBlock -> "numbered_list_item"
                is ToDoBlock -> "to_do"
                is ToggleBlock -> "toggle"
                is CodeBlock -> "code"
                is BookmarkBlock -> "bookmark"
                is EquationBlock -> "equation"
                is QuoteBlock -> "quote"
                is EmbedBlock -> "embed"
                is CalloutBlock -> "callout"
                is DividerBlock -> "divider"
                is TableOfContentsBlock -> "table_of_contents"

                else -> throw IllegalStateException()
            }
            put("type", type)
            putJsonObject(type) {
                model.text?.let { text(it) }
                when (model) {
                    is ToDoBlock -> put("checked", model.checked)
                    is CodeBlock -> put("language", model.language)
                    is BookmarkBlock -> {
                        put("url", model.url)
                        model.caption?.let {
                            put("caption", it.modelToApi(ApiOutRichTextListConverter))
                        }
                    }
                    is EquationBlock -> put("expression", model.expression)
                    is EmbedBlock -> put("url", model.url)
                    is CalloutBlock -> model.icon?.let {
                        put("icon", it.modelToApi(ApiOutEmojiOrFileConverter))
                    }
                    else -> {
                    }
                }
                model.children?.let {
                    if (it.isNotEmpty()) put("children", JsonArray(it.modelToApi(ApiOutBlockConverter)))
                }
            }
        }
    }

    private fun JsonObjectBuilder.text(richTextList: RichTextList) =
        put("text", richTextList.modelToApi(ApiOutRichTextListConverter))
}

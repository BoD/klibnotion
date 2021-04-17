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

package org.jraf.klibnotion.internal.api.model.content.value

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.modelToApi
import org.jraf.klibnotion.internal.api.model.richtext.ApiOutRichTextListConverter
import org.jraf.klibnotion.model.block.value.BlockValue
import org.jraf.klibnotion.model.block.value.BulletedListItemBlockValue
import org.jraf.klibnotion.model.block.value.Heading1BlockValue
import org.jraf.klibnotion.model.block.value.Heading2BlockValue
import org.jraf.klibnotion.model.block.value.Heading3BlockValue
import org.jraf.klibnotion.model.block.value.NumberedListItemBlockValue
import org.jraf.klibnotion.model.block.value.ParagraphBlockValue
import org.jraf.klibnotion.model.block.value.ToDoBlockValue
import org.jraf.klibnotion.model.block.value.ToggleBlockValue
import org.jraf.klibnotion.model.richtext.RichTextList

internal object ApiOutContentValueConverter : ApiConverter<JsonElement, BlockValue>() {

    override fun modelToApi(model: BlockValue): JsonElement {
        return buildJsonObject {
            put("object", "block")

            val type = when (model) {
                is ParagraphBlockValue -> "paragraph"
                is Heading1BlockValue -> "heading_1"
                is Heading2BlockValue -> "heading_2"
                is Heading3BlockValue -> "heading_3"
                is BulletedListItemBlockValue -> "bulleted_list_item"
                is NumberedListItemBlockValue -> "numbered_list_item"
                is ToDoBlockValue -> "to_do"
                is ToggleBlockValue -> "toggle"

                else -> throw IllegalStateException()
            }
            put("type", type)
            putJsonObject(type) {
                text(model.text)
                when (model) {
                    is ToDoBlockValue -> put("checked", model.checked)
                }
                model.content?.let {
                    put("children", JsonArray(it.blockValueList.modelToApi(ApiOutContentValueConverter)))
                }
            }
        }
    }

    private fun JsonObjectBuilder.text(richTextList: RichTextList) =
        put("text", richTextList.modelToApi(ApiOutRichTextListConverter))
}

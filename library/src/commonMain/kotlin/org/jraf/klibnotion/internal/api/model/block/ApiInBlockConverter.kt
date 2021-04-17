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

package org.jraf.klibnotion.internal.api.model.block

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.apiToModel
import org.jraf.klibnotion.internal.api.model.richtext.ApiRichTextConverter
import org.jraf.klibnotion.internal.model.content.value.BulletedListItemBlockImpl
import org.jraf.klibnotion.internal.model.content.value.ChildPageBlockImpl
import org.jraf.klibnotion.internal.model.content.value.Heading1BlockImpl
import org.jraf.klibnotion.internal.model.content.value.Heading2BlockImpl
import org.jraf.klibnotion.internal.model.content.value.Heading3BlockImpl
import org.jraf.klibnotion.internal.model.content.value.NumberedListItemBlockImpl
import org.jraf.klibnotion.internal.model.content.value.ParagraphBlockImpl
import org.jraf.klibnotion.internal.model.content.value.ToDoBlockImpl
import org.jraf.klibnotion.internal.model.content.value.ToggleBlockImpl
import org.jraf.klibnotion.internal.model.content.value.UnknownTypeBlockImpl
import org.jraf.klibnotion.model.block.Block
import org.jraf.klibnotion.model.richtext.RichTextList

internal object ApiInBlockConverter : ApiConverter<ApiBlock, Block>() {
    override fun apiToModel(apiModel: ApiBlock): Block {
        val id = apiModel.id
        return when (val type = apiModel.type) {
            "paragraph" -> ParagraphBlockImpl(
                id = id,
                text = apiModel.paragraph.toRichTextList(),
                children = null
            )

            "heading_1" -> Heading1BlockImpl(
                id = id,
                text = apiModel.heading_1.toRichTextList()
            )

            "heading_2" -> Heading2BlockImpl(
                id = id,
                text = apiModel.heading_2.toRichTextList()
            )

            "heading_3" -> Heading3BlockImpl(
                id = id,
                text = apiModel.heading_3.toRichTextList()
            )

            "bulleted_list_item" -> BulletedListItemBlockImpl(
                id = id,
                text = apiModel.bulleted_list_item.toRichTextList(),
                children = null
            )

            "numbered_list_item" -> NumberedListItemBlockImpl(
                id = id,
                text = apiModel.numbered_list_item.toRichTextList(),
                children = null
            )

            "to_do" -> ToDoBlockImpl(
                id = id,
                text = apiModel.to_do.toRichTextList(),
                checked = apiModel.to_do!!.checked,
                children = null
            )

            "toggle" -> ToggleBlockImpl(
                id = id,
                text = apiModel.toggle.toRichTextList(),
                children = null
            )

            "child_page" -> ChildPageBlockImpl(
                id = id,
                title = apiModel.child_page!!.title
            )

            else -> UnknownTypeBlockImpl(
                id = id,
                type = type,
            )
        }
    }

    private fun ApiBlockText?.toRichTextList() = RichTextList(this!!.text.apiToModel(ApiRichTextConverter))
    private fun ApiBlockTodo?.toRichTextList() = RichTextList(this!!.text.apiToModel(ApiRichTextConverter))

}
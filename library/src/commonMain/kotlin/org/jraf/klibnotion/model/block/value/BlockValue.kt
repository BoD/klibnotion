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

package org.jraf.klibnotion.model.block.value

import org.jraf.klibnotion.internal.model.content.value.BulletedListItemBlockValueImpl
import org.jraf.klibnotion.internal.model.content.value.Heading1BlockValueImpl
import org.jraf.klibnotion.internal.model.content.value.Heading2BlockValueImpl
import org.jraf.klibnotion.internal.model.content.value.Heading3BlockValueImpl
import org.jraf.klibnotion.internal.model.content.value.NumberedListItemBlockValueImpl
import org.jraf.klibnotion.internal.model.content.value.ParagraphBlockValueImpl
import org.jraf.klibnotion.internal.model.content.value.ToDoBlockValueImpl
import org.jraf.klibnotion.internal.model.content.value.ToggleBlockValueImpl
import org.jraf.klibnotion.model.richtext.Annotations
import org.jraf.klibnotion.model.richtext.RichTextList
import kotlin.jvm.JvmOverloads

/**
 * See [https://www.notion.so/notiondevs/fa3660a1844b451aa99e9aac965438c1?v=9b36837a440f448cbd2dd39f12edcfba].
 */
interface BlockValue {
    val text: RichTextList
    val content: BlockValueList?
}

class BlockValueList {
    internal val blockValueList = mutableListOf<BlockValue>()

    private fun add(blockValue: BlockValue): BlockValueList {
        blockValueList.add(blockValue)
        return this
    }

    fun paragraph(text: RichTextList, content: BlockValueListProducer? = null): BlockValueList =
        add(ParagraphBlockValueImpl(text, content()))

    @JvmOverloads
    fun paragraph(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        content: BlockValueListProducer? = null,
    ): BlockValueList = paragraph(
        text = RichTextList().text(text, linkUrl, annotations),
        content = content,
    )


    fun heading1(text: RichTextList): BlockValueList = add(Heading1BlockValueImpl(text))

    @JvmOverloads
    fun heading1(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): BlockValueList = add(Heading1BlockValueImpl(
        RichTextList().text(
            text,
            linkUrl,
            annotations)
    ))

    fun heading2(text: RichTextList): BlockValueList = add(Heading1BlockValueImpl(text))

    @JvmOverloads
    fun heading2(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): BlockValueList = add(Heading2BlockValueImpl(
        RichTextList().text(
            text,
            linkUrl,
            annotations)
    ))

    fun heading3(text: RichTextList): BlockValueList = add(Heading1BlockValueImpl(text))

    @JvmOverloads
    fun heading3(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): BlockValueList = add(Heading3BlockValueImpl(
        RichTextList().text(
            text,
            linkUrl,
            annotations)
    ))

    fun bullet(text: RichTextList, content: BlockValueListProducer? = null): BlockValueList =
        add(BulletedListItemBlockValueImpl(text, content()))

    @JvmOverloads
    fun bullet(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        content: BlockValueListProducer? = null,
    ): BlockValueList = bullet(
        text = RichTextList().text(text, linkUrl, annotations),
        content = content,
    )

    fun number(text: RichTextList, content: BlockValueListProducer? = null): BlockValueList =
        add(NumberedListItemBlockValueImpl(text, content()))

    @JvmOverloads
    fun number(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        content: BlockValueListProducer? = null,
    ): BlockValueList = number(
        text = RichTextList().text(text, linkUrl, annotations),
        content = content,
    )

    fun toDo(text: RichTextList, checked: Boolean, content: BlockValueListProducer? = null): BlockValueList =
        add(ToDoBlockValueImpl(text, checked, content()))

    @JvmOverloads
    fun toDo(
        text: String,
        checked: Boolean,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        content: BlockValueListProducer? = null,
    ): BlockValueList = toDo(
        text = RichTextList().text(text, linkUrl, annotations),
        checked = checked,
        content = content,
    )

    fun toggle(text: RichTextList, content: BlockValueListProducer? = null): BlockValueList =
        add(ToggleBlockValueImpl(text, content()))

    @JvmOverloads
    fun toggle(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        content: BlockValueListProducer? = null,
    ): BlockValueList = toggle(
        text = RichTextList().text(text, linkUrl, annotations),
        content = content,
    )

}

typealias BlockValueListProducer = BlockValueList.() -> Unit

internal operator fun BlockValueListProducer?.invoke() = this?.let { producer ->
    BlockValueList().apply { producer(this) }
}


fun content(content: BlockValueListProducer) = content()

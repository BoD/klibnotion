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

package org.jraf.klibnotion.model.block

import org.jraf.klibnotion.internal.model.block.BulletedListItemBlockImpl
import org.jraf.klibnotion.internal.model.block.Heading1BlockImpl
import org.jraf.klibnotion.internal.model.block.Heading2BlockImpl
import org.jraf.klibnotion.internal.model.block.Heading3BlockImpl
import org.jraf.klibnotion.internal.model.block.NumberedListItemBlockImpl
import org.jraf.klibnotion.internal.model.block.ParagraphBlockImpl
import org.jraf.klibnotion.internal.model.block.ToDoBlockImpl
import org.jraf.klibnotion.internal.model.block.ToggleBlockImpl
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.richtext.Annotations
import org.jraf.klibnotion.model.richtext.RichTextList
import kotlin.jvm.JvmOverloads

/**
 * See [https://developers.notion.com/reference/block].
 */
sealed interface Block {
    val id: UuidString
    val text: RichTextList?
    val children: List<Block>?
}

class MutableBlockList(
    private val blockList: MutableList<Block> = mutableListOf(),
) : List<Block> by blockList {

    private fun add(block: Block): MutableBlockList {
        blockList.add(block)
        return this
    }

    fun paragraph(richTextList: RichTextList, children: BlockListProducer? = null): MutableBlockList =
        add(ParagraphBlockImpl(id = "", richTextList, children()))

    @JvmOverloads
    fun paragraph(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        children: BlockListProducer? = null,
    ): MutableBlockList = paragraph(
        richTextList = RichTextList().text(text, linkUrl, annotations),
        children = children,
    )


    fun heading1(richTextList: RichTextList): MutableBlockList = add(Heading1BlockImpl(id = "", richTextList))

    @JvmOverloads
    fun heading1(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): MutableBlockList = add(Heading1BlockImpl(id = "",
        RichTextList().text(
            text,
            linkUrl,
            annotations)
    ))

    fun heading2(richTextList: RichTextList): MutableBlockList = add(Heading1BlockImpl(id = "", richTextList))

    @JvmOverloads
    fun heading2(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): MutableBlockList = add(Heading2BlockImpl(id = "",
        RichTextList().text(
            text,
            linkUrl,
            annotations)
    ))

    fun heading3(richTextList: RichTextList): MutableBlockList = add(Heading1BlockImpl(id = "", richTextList))

    @JvmOverloads
    fun heading3(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): MutableBlockList = add(Heading3BlockImpl(id = "",
        RichTextList().text(
            text,
            linkUrl,
            annotations)
    ))

    fun bullet(richTextList: RichTextList, children: BlockListProducer? = null): MutableBlockList =
        add(BulletedListItemBlockImpl(id = "", richTextList, children()))

    @JvmOverloads
    fun bullet(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        children: BlockListProducer? = null,
    ): MutableBlockList = bullet(
        richTextList = RichTextList().text(text, linkUrl, annotations),
        children = children,
    )

    fun number(richTextList: RichTextList, children: BlockListProducer? = null): MutableBlockList =
        add(NumberedListItemBlockImpl(id = "", richTextList, children()))

    @JvmOverloads
    fun number(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        children: BlockListProducer? = null,
    ): MutableBlockList = number(
        richTextList = RichTextList().text(text, linkUrl, annotations),
        children = children,
    )

    fun toDo(richTextList: RichTextList, checked: Boolean, children: BlockListProducer? = null): MutableBlockList =
        add(ToDoBlockImpl(id = "", richTextList, checked, children()))

    @JvmOverloads
    fun toDo(
        text: String,
        checked: Boolean,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        children: BlockListProducer? = null,
    ): MutableBlockList = toDo(
        richTextList = RichTextList().text(text, linkUrl, annotations),
        checked = checked,
        children = children,
    )

    fun toggle(richTextList: RichTextList, children: BlockListProducer? = null): MutableBlockList =
        add(ToggleBlockImpl(id = "", richTextList, children()))

    @JvmOverloads
    fun toggle(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        children: BlockListProducer? = null,
    ): MutableBlockList = toggle(
        richTextList = RichTextList().text(text, linkUrl, annotations),
        children = children,
    )

}

typealias BlockListProducer = MutableBlockList.() -> Unit

internal operator fun BlockListProducer?.invoke() = this?.let { producer ->
    MutableBlockList().apply { producer(this) }
}


fun content(content: BlockListProducer) = content()

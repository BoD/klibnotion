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

package org.jraf.klibnotion.model.block

import org.jraf.klibnotion.internal.IRRELEVANT_TIMESTAMP
import org.jraf.klibnotion.internal.model.block.BookmarkBlockImpl
import org.jraf.klibnotion.internal.model.block.BulletedListItemBlockImpl
import org.jraf.klibnotion.internal.model.block.CalloutBlockImpl
import org.jraf.klibnotion.internal.model.block.CodeBlockImpl
import org.jraf.klibnotion.internal.model.block.DividerBlockImpl
import org.jraf.klibnotion.internal.model.block.EmbedBlockImpl
import org.jraf.klibnotion.internal.model.block.EquationBlockImpl
import org.jraf.klibnotion.internal.model.block.Heading1BlockImpl
import org.jraf.klibnotion.internal.model.block.Heading2BlockImpl
import org.jraf.klibnotion.internal.model.block.Heading3BlockImpl
import org.jraf.klibnotion.internal.model.block.ImageBlockImpl
import org.jraf.klibnotion.internal.model.block.NumberedListItemBlockImpl
import org.jraf.klibnotion.internal.model.block.ParagraphBlockImpl
import org.jraf.klibnotion.internal.model.block.QuoteBlockImpl
import org.jraf.klibnotion.internal.model.block.TableOfContentsBlockImpl
import org.jraf.klibnotion.internal.model.block.ToDoBlockImpl
import org.jraf.klibnotion.internal.model.block.ToggleBlockImpl
import org.jraf.klibnotion.internal.model.block.VideoBlockImpl
import org.jraf.klibnotion.internal.model.file.FileImpl
import org.jraf.klibnotion.model.base.EmojiOrFile
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.date.Timestamp
import org.jraf.klibnotion.model.richtext.Annotations
import org.jraf.klibnotion.model.richtext.RichTextList
import kotlin.jvm.JvmOverloads

/**
 * See [Reference](https://developers.notion.com/reference/block).
 */
sealed interface Block {
    val id: UuidString
    val created: Timestamp
    val lastEdited: Timestamp
    val children: List<Block>?
}

class MutableBlockList(
    private val blockList: MutableList<Block> = mutableListOf(),
) : List<Block> by blockList {

    private fun add(block: Block): MutableBlockList {
        blockList.add(block)
        return this
    }

    @JvmOverloads
    fun paragraph(richTextList: RichTextList, children: BlockListProducer? = null): MutableBlockList =
        add(ParagraphBlockImpl(
            id = "",
            created = IRRELEVANT_TIMESTAMP,
            lastEdited = IRRELEVANT_TIMESTAMP,
            richTextList,
            children()
        ))

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

    @JvmOverloads
    fun heading1(richTextList: RichTextList): MutableBlockList =
        add(org.jraf.klibnotion.model.block.heading1(richTextList))

    @JvmOverloads
    fun heading1(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): MutableBlockList = add(org.jraf.klibnotion.model.block.heading1(text, linkUrl, annotations))

    fun heading2(richTextList: RichTextList): MutableBlockList =
        add(org.jraf.klibnotion.model.block.heading2(richTextList))

    @JvmOverloads
    fun heading2(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): MutableBlockList = add(org.jraf.klibnotion.model.block.heading2(text, linkUrl, annotations))

    fun heading3(richTextList: RichTextList): MutableBlockList =
        add(org.jraf.klibnotion.model.block.heading3(richTextList))

    @JvmOverloads
    fun heading3(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): MutableBlockList = add(org.jraf.klibnotion.model.block.heading3(text, linkUrl, annotations))

    @JvmOverloads
    fun bullet(richTextList: RichTextList, children: BlockListProducer? = null): MutableBlockList =
        add(BulletedListItemBlockImpl(
            id = "",
            created = IRRELEVANT_TIMESTAMP,
            lastEdited = IRRELEVANT_TIMESTAMP,
            richTextList,
            children()))

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

    @JvmOverloads
    fun number(richTextList: RichTextList, children: BlockListProducer? = null): MutableBlockList =
        add(NumberedListItemBlockImpl(
            id = "",
            created = IRRELEVANT_TIMESTAMP,
            lastEdited = IRRELEVANT_TIMESTAMP,
            richTextList,
            children()
        ))

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

    @JvmOverloads
    fun toDo(richTextList: RichTextList, checked: Boolean, children: BlockListProducer? = null): MutableBlockList =
        add(ToDoBlockImpl(
            id = "",
            created = IRRELEVANT_TIMESTAMP,
            lastEdited = IRRELEVANT_TIMESTAMP,
            richTextList,
            checked,
            children()
        ))

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

    @JvmOverloads
    fun toggle(richTextList: RichTextList, children: BlockListProducer? = null): MutableBlockList =
        add(ToggleBlockImpl(
            id = "",
            created = IRRELEVANT_TIMESTAMP,
            lastEdited = IRRELEVANT_TIMESTAMP,
            richTextList,
            children()
        ))

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

    @JvmOverloads
    fun bookmark(
        url: String,
        caption: RichTextList? = null,
    ): MutableBlockList = add(org.jraf.klibnotion.model.block.bookmark(url, caption))

    @JvmOverloads
    fun bookmark(
        url: String,
        caption: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): MutableBlockList = add(org.jraf.klibnotion.model.block.bookmark(url, caption, linkUrl, annotations))

    fun code(
        language: String,
        text: RichTextList,
    ): MutableBlockList = add(org.jraf.klibnotion.model.block.code(language, text))

    @JvmOverloads
    fun code(
        language: String,
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): MutableBlockList = add(org.jraf.klibnotion.model.block.code(language, text, linkUrl, annotations))

    fun equation(
        expression: String,
    ): MutableBlockList = add(org.jraf.klibnotion.model.block.equation(expression))

    @JvmOverloads
    fun quote(
        richTextList: RichTextList,
        children: BlockListProducer? = null,
    ): MutableBlockList =
        add(QuoteBlockImpl(
            id = "",
            created = IRRELEVANT_TIMESTAMP,
            lastEdited = IRRELEVANT_TIMESTAMP,
            text = richTextList,
            children = children()
        ))


    @JvmOverloads
    fun quote(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        children: BlockListProducer? = null,
    ): MutableBlockList = quote(
        richTextList = RichTextList().text(text, linkUrl, annotations),
        children = children,
    )

    fun embed(url: String): MutableBlockList = add(org.jraf.klibnotion.model.block.embed(url))

    @JvmOverloads
    fun callout(
        richTextList: RichTextList,
        icon: EmojiOrFile? = null,
        children: BlockListProducer? = null,
    ): MutableBlockList = add(CalloutBlockImpl(
        id = "",
        created = IRRELEVANT_TIMESTAMP,
        lastEdited = IRRELEVANT_TIMESTAMP,
        children = children(),
        text = richTextList,
        icon = icon
    ))

    @JvmOverloads
    fun callout(
        text: String,
        icon: EmojiOrFile? = null,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        children: BlockListProducer? = null,
    ): MutableBlockList =
        callout(
            richTextList = RichTextList().text(text, linkUrl, annotations),
            icon = icon,
            children = children
        )

    fun divider(): MutableBlockList = add(org.jraf.klibnotion.model.block.divider())

    fun tableOfContents(): MutableBlockList = add(org.jraf.klibnotion.model.block.tableOfContents())

    fun image(
        url: String,
        caption: RichTextList? = null,
    ) = add(org.jraf.klibnotion.model.block.image(url, caption))

    @JvmOverloads
    fun image(
        url: String,
        caption: String? = null,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ) = add(org.jraf.klibnotion.model.block.image(url, caption, linkUrl, annotations))

    fun video(
        url: String,
        caption: RichTextList? = null,
    ) = add(org.jraf.klibnotion.model.block.video(url, caption))

    @JvmOverloads
    fun video(
        url: String,
        caption: String? = null,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ) = add(org.jraf.klibnotion.model.block.video(url, caption, linkUrl, annotations))
}

typealias BlockListProducer = MutableBlockList.() -> Unit

internal operator fun BlockListProducer?.invoke() = this?.let { producer ->
    MutableBlockList().apply { producer(this) }
}


fun content(content: BlockListProducer) = content()


fun paragraph(richTextList: RichTextList): Block = ParagraphBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    richTextList,
    null,
)

@JvmOverloads
fun paragraph(
    text: String,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = paragraph(
    richTextList = RichTextList().text(text, linkUrl, annotations),
)

fun heading1(richTextList: RichTextList): Block = Heading1BlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    richTextList
)

@JvmOverloads
fun heading1(
    text: String,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = heading1(
    richTextList = RichTextList().text(text, linkUrl, annotations),
)

fun heading2(richTextList: RichTextList): Block = Heading2BlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    richTextList
)

@JvmOverloads
fun heading2(
    text: String,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = heading2(
    richTextList = RichTextList().text(text, linkUrl, annotations)
)

fun heading3(richTextList: RichTextList): Block = Heading3BlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    richTextList
)

@JvmOverloads
fun heading3(
    text: String,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = heading3(
    richTextList = RichTextList().text(text, linkUrl, annotations)
)

fun bulletedListItem(richTextList: RichTextList): Block = BulletedListItemBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    richTextList,
    null,
)

@JvmOverloads
fun bulletedListItem(
    text: String,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = bulletedListItem(
    richTextList = RichTextList().text(text, linkUrl, annotations),
)

fun numberedListItem(richTextList: RichTextList): Block = NumberedListItemBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    richTextList,
    null
)

@JvmOverloads
fun numberedListItem(
    text: String,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = numberedListItem(
    richTextList = RichTextList().text(text, linkUrl, annotations),
)

fun toDo(richTextList: RichTextList, checked: Boolean): Block = ToDoBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    richTextList,
    checked,
    null
)

@JvmOverloads
fun toDo(
    text: String,
    checked: Boolean,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = toDo(
    richTextList = RichTextList().text(text, linkUrl, annotations),
    checked = checked,
)

fun toggle(richTextList: RichTextList): Block = ToggleBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    richTextList,
    null,
)

@JvmOverloads
fun toggle(
    text: String,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = toggle(
    richTextList = RichTextList().text(text, linkUrl, annotations),
)

fun bookmark(
    url: String,
    caption: RichTextList? = null,
): Block = BookmarkBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    url = url,
    caption = caption
)

@JvmOverloads
fun bookmark(
    url: String,
    caption: String? = null,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = bookmark(
    url = url,
    caption = caption?.let { RichTextList().text(it, linkUrl, annotations) }
)

fun code(
    language: String,
    text: RichTextList,
): Block = CodeBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    text = text,
    language = language
)

@JvmOverloads
fun code(
    language: String,
    text: String,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = code(
    language = language,
    text = RichTextList().text(text, linkUrl, annotations),
)

fun equation(
    expression: String,
): Block = EquationBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    expression = expression
)

fun quote(richTextList: RichTextList): Block = QuoteBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    text = richTextList,
    children = null
)

@JvmOverloads
fun quote(
    text: String,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = quote(
    RichTextList().text(text, linkUrl, annotations)
)

fun embed(url: String): Block = EmbedBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    url = url
)

fun callout(
    richTextList: RichTextList,
    icon: EmojiOrFile? = null,
): Block = CalloutBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    children = null,
    text = richTextList,
    icon = icon
)

@JvmOverloads
fun callout(
    text: String,
    icon: EmojiOrFile? = null,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = callout(
    richTextList = RichTextList().text(text, linkUrl, annotations),
    icon = icon
)


fun divider(): Block = DividerBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
)

fun tableOfContents(): Block = TableOfContentsBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP
)

fun image(
    url: String,
    caption: RichTextList? = null,
): Block = ImageBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    image = FileImpl(name = "image", url = url),
    caption = caption,
)

@JvmOverloads
fun image(
    url: String,
    caption: String? = null,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = image(
    url = url,
    caption = caption?.let { RichTextList().text(it, linkUrl, annotations) },
)

fun video(
    url: String,
    caption: RichTextList? = null,
): Block = VideoBlockImpl(
    id = "",
    created = IRRELEVANT_TIMESTAMP,
    lastEdited = IRRELEVANT_TIMESTAMP,
    video = FileImpl(name = "video", url = url),
    caption = caption,
)

@JvmOverloads
fun video(
    url: String,
    caption: String? = null,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
): Block = video(
    url = url,
    caption = caption?.let { RichTextList().text(it, linkUrl, annotations) },
)
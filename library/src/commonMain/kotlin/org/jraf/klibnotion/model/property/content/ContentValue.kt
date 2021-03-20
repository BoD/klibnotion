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

package org.jraf.klibnotion.model.property.content

import org.jraf.klibnotion.internal.model.content.value.BulletedListItemContentValueImpl
import org.jraf.klibnotion.internal.model.content.value.Heading1ContentValueImpl
import org.jraf.klibnotion.internal.model.content.value.Heading2ContentValueImpl
import org.jraf.klibnotion.internal.model.content.value.Heading3ContentValueImpl
import org.jraf.klibnotion.internal.model.content.value.NumberedListItemContentValueImpl
import org.jraf.klibnotion.internal.model.content.value.ParagraphContentValueImpl
import org.jraf.klibnotion.internal.model.content.value.ToDoContentValueImpl
import org.jraf.klibnotion.internal.model.content.value.ToggleContentValueImpl
import org.jraf.klibnotion.model.richtext.Annotations
import org.jraf.klibnotion.model.richtext.RichTextList
import kotlin.jvm.JvmOverloads

/**
 * See [https://www.notion.so/notiondevs/fa3660a1844b451aa99e9aac965438c1?v=9b36837a440f448cbd2dd39f12edcfba].
 */
interface ContentValue {
    val text: RichTextList
    val content: ContentValueList?
}

class ContentValueList {
    internal val contentValueList = mutableListOf<ContentValue>()

    private fun add(propertyValue: ContentValue): ContentValueList {
        contentValueList.add(propertyValue)
        return this
    }

    fun paragraph(text: RichTextList, content: ContentValueListProducer? = null): ContentValueList =
        add(ParagraphContentValueImpl(text, content()))

    @JvmOverloads
    fun paragraph(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        content: ContentValueListProducer? = null,
    ): ContentValueList = paragraph(
        text = RichTextList().text(text, linkUrl, annotations),
        content = content,
    )


    fun heading1(text: RichTextList): ContentValueList = add(Heading1ContentValueImpl(text))

    @JvmOverloads
    fun heading1(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): ContentValueList = add(Heading1ContentValueImpl(
        RichTextList().text(
            text,
            linkUrl,
            annotations)
    ))

    fun heading2(text: RichTextList): ContentValueList = add(Heading1ContentValueImpl(text))

    @JvmOverloads
    fun heading2(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): ContentValueList = add(Heading2ContentValueImpl(
        RichTextList().text(
            text,
            linkUrl,
            annotations)
    ))

    fun heading3(text: RichTextList): ContentValueList = add(Heading1ContentValueImpl(text))

    @JvmOverloads
    fun heading3(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): ContentValueList = add(Heading3ContentValueImpl(
        RichTextList().text(
            text,
            linkUrl,
            annotations)
    ))

    fun bullet(text: RichTextList, content: ContentValueListProducer? = null): ContentValueList =
        add(BulletedListItemContentValueImpl(text, content()))

    @JvmOverloads
    fun bullet(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        content: ContentValueListProducer? = null,
    ): ContentValueList = bullet(
        text = RichTextList().text(text, linkUrl, annotations),
        content = content,
    )

    fun number(text: RichTextList, content: ContentValueListProducer? = null): ContentValueList =
        add(NumberedListItemContentValueImpl(text, content()))

    @JvmOverloads
    fun number(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        content: ContentValueListProducer? = null,
    ): ContentValueList = number(
        text = RichTextList().text(text, linkUrl, annotations),
        content = content,
    )

    fun toDo(text: RichTextList, checked: Boolean, content: ContentValueListProducer? = null): ContentValueList =
        add(ToDoContentValueImpl(text, checked, content()))

    @JvmOverloads
    fun toDo(
        text: String,
        checked: Boolean,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        content: ContentValueListProducer? = null,
    ): ContentValueList = toDo(
        text = RichTextList().text(text, linkUrl, annotations),
        checked = checked,
        content = content,
    )

    fun toggle(text: RichTextList, content: ContentValueListProducer? = null): ContentValueList =
        add(ToggleContentValueImpl(text, content()))

    @JvmOverloads
    fun toggle(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
        content: ContentValueListProducer? = null,
    ): ContentValueList = toggle(
        text = RichTextList().text(text, linkUrl, annotations),
        content = content,
    )

}

typealias ContentValueListProducer = ContentValueList.() -> Unit

internal operator fun ContentValueListProducer?.invoke() = this?.let { producer ->
    ContentValueList().apply { producer(this) }
}


fun content(content: ContentValueListProducer) = content()

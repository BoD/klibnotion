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
 * and contributors (https://github.com/BoD/klibnotion/graphs/contributors)
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

package org.jraf.klibnotion.model.richtext

import org.jraf.klibnotion.internal.model.richtext.EquationRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.TextRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.mention.DatabaseMentionRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.mention.DateMentionRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.mention.PageMentionRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.mention.UserMentionRichTextImpl
import org.jraf.klibnotion.internal.model.user.PersonImpl
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.date.DateOrDateRange
import org.jraf.klibnotion.model.date.DateOrDateTime

class RichTextList(
    richTextList: List<RichText> = emptyList(),
) {
    val richTextList: MutableList<RichText> = richTextList.toMutableList()

    val plainText: String?
        get() = if (richTextList.isEmpty()) {
            null
        } else {
            richTextList.joinToString(separator = "") { it.plainText }
        }

    private fun add(richText: RichText): RichTextList = apply {
        richTextList.add(richText)
    }

    fun text(
        text: String,
        annotations: Annotations = Annotations.DEFAULT,
    ) = text(text = text, linkUrl = null, annotations = annotations)

    fun text(
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ) = add(TextRichTextImpl(
        plainText = text,
        href = null,
        annotations = annotations,
        linkUrl = linkUrl,
    ))

    fun userMention(
        userId: UuidString,
        annotations: Annotations = Annotations.DEFAULT,
    ) = add(UserMentionRichTextImpl(
        plainText = "",
        href = null,
        annotations = annotations,
        user = PersonImpl(userId, "", null, "")
    ))

    fun pageMention(
        pageId: UuidString,
        annotations: Annotations = Annotations.DEFAULT,
    ) = add(PageMentionRichTextImpl(
        plainText = "",
        href = null,
        annotations = annotations,
        pageId = pageId
    ))

    fun databaseMention(
        databaseId: UuidString,
        annotations: Annotations = Annotations.DEFAULT,
    ) = add(DatabaseMentionRichTextImpl(
        plainText = "",
        href = null,
        annotations = annotations,
        databaseId = databaseId
    ))

    fun dateMention(
        start: DateOrDateTime,
        end: DateOrDateTime? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ) = add(DateMentionRichTextImpl(
        plainText = "",
        href = null,
        annotations = annotations,
        dateOrDateRange = DateOrDateRange(start, end)
    ))

    fun equation(
        expression: String,
        annotations: Annotations = Annotations.DEFAULT,
    ) = add(EquationRichTextImpl(
        plainText = "",
        href = null,
        annotations = annotations,
        expression = expression
    ))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as RichTextList

        if (richTextList != other.richTextList) return false

        return true
    }

    override fun hashCode(): Int {
        return richTextList.hashCode()
    }

    override fun toString(): String {
        return "RichTextList(richTextList=$richTextList, plainText=$plainText)"
    }
}

fun text(text: String) = RichTextList().text(text)

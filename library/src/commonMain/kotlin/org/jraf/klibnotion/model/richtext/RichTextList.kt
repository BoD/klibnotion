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

package org.jraf.klibnotion.model.richtext

import org.jraf.klibnotion.internal.model.richtext.EquationRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.RichTextListImpl
import org.jraf.klibnotion.internal.model.richtext.TextRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.mention.DatabaseMentionRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.mention.DateMentionRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.mention.PageMentionRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.mention.UserMentionRichTextImpl
import org.jraf.klibnotion.internal.model.user.PersonImpl
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.date.DateOrDateRange
import org.jraf.klibnotion.model.date.DateOrDateTime

interface RichTextList : List<RichText> {
    val plainText: String?
}

fun richTextList(): RichTextList = RichTextListImpl(emptyList())

private fun RichTextList.add(richText: RichText): RichTextList = (this as RichTextListImpl).apply {
    add(richText)
}

fun RichTextList.text(
    text: String,
    annotations: Annotations = Annotations.DEFAULT,
) = text(text = text, linkUrl = null, annotations = annotations)

fun RichTextList.text(
    text: String,
    linkUrl: String? = null,
    annotations: Annotations = Annotations.DEFAULT,
) = add(TextRichTextImpl(
    plainText = text,
    href = null,
    annotations = annotations,
    linkUrl = linkUrl,
))

fun RichTextList.userMention(
    userId: UuidString,
    annotations: Annotations = Annotations.DEFAULT,
) = add(UserMentionRichTextImpl(
    plainText = "",
    href = null,
    annotations = annotations,
    user = PersonImpl(userId, "", null, "")
))

fun RichTextList.pageMention(
    pageId: UuidString,
    annotations: Annotations = Annotations.DEFAULT,
) = add(PageMentionRichTextImpl(
    plainText = "",
    href = null,
    annotations = annotations,
    pageId = pageId
))

fun RichTextList.databaseMention(
    databaseId: UuidString,
    annotations: Annotations = Annotations.DEFAULT,
) = add(DatabaseMentionRichTextImpl(
    plainText = "",
    href = null,
    annotations = annotations,
    databaseId = databaseId
))

fun RichTextList.dateMention(
    start: DateOrDateTime,
    end: DateOrDateTime? = null,
    annotations: Annotations = Annotations.DEFAULT,
) = add(DateMentionRichTextImpl(
    plainText = "",
    href = null,
    annotations = annotations,
    dateOrDateRange = DateOrDateRange(start, end)
))

fun RichTextList.equation(
    expression: String,
    annotations: Annotations = Annotations.DEFAULT,
) = add(EquationRichTextImpl(
    plainText = "",
    href = null,
    annotations = annotations,
    expression = expression
))
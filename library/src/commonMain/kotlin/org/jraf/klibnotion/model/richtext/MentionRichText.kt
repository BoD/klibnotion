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

import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.date.DateOrDateRange
import org.jraf.klibnotion.model.user.User

/**
 * See [Reference](https://developers.notion.com/reference/rich-text).
 */
sealed interface MentionRichText : RichText

/**
 * See [Reference](https://developers.notion.com/reference/rich-text).
 */
interface DatabaseMentionRichText : MentionRichText {
    val databaseId: UuidString
}

/**
 * See [Reference](https://developers.notion.com/reference/rich-text).
 */
interface DateMentionRichText : MentionRichText {
    val dateOrDateRange: DateOrDateRange
}

/**
 * See [Reference](https://developers.notion.com/reference/rich-text).
 */
interface PageMentionRichText : MentionRichText {
    val pageId: UuidString
}

/**
 * This type is returned when a Mention Rich Text of a type unknown to this library is returned by the Notion API.
 *
 * See [Reference](https://developers.notion.com/reference/rich-text).
 */
interface UnknownTypeMentionRichText : MentionRichText {
    val type: String
}

/**
 * See [Reference](https://developers.notion.com/reference/rich-text).
 */
interface UserMentionRichText : MentionRichText {
    val user: User
}
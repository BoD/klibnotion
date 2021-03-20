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

package org.jraf.klibnotion.internal.api.model.richtext

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.color.ApiColorConverter
import org.jraf.klibnotion.internal.api.model.date.ApiDateStringConverter
import org.jraf.klibnotion.internal.api.model.modelToApi
import org.jraf.klibnotion.model.color.Color
import org.jraf.klibnotion.model.richtext.Annotations
import org.jraf.klibnotion.model.richtext.EquationRichText
import org.jraf.klibnotion.model.richtext.RichTextList
import org.jraf.klibnotion.model.richtext.TextRichText
import org.jraf.klibnotion.model.richtext.mention.DatabaseMentionRichText
import org.jraf.klibnotion.model.richtext.mention.DateMentionRichText
import org.jraf.klibnotion.model.richtext.mention.PageMentionRichText
import org.jraf.klibnotion.model.richtext.mention.UserMentionRichText

internal object ApiOutRichTextListConverter : ApiConverter<JsonArray, RichTextList>() {
    override fun modelToApi(model: RichTextList): JsonArray = buildJsonArray {
        for (richText in model.richTextList) {
            addJsonObject {
                if (richText.annotations != Annotations.DEFAULT) {
                    putJsonObject("annotations") {
                        if (richText.annotations.bold) put("bold", true)
                        if (richText.annotations.italic) put("italic", true)
                        if (richText.annotations.strikethrough) put("strikethrough", true)
                        if (richText.annotations.underline) put("underline", true)
                        if (richText.annotations.code) put("code", true)
                        if (richText.annotations.color != Color.DEFAULT) {
                            put("color", richText.annotations.color.modelToApi(ApiColorConverter))
                        }
                    }
                }

                when (richText) {
                    is TextRichText -> putJsonObject("text") {
                        put("content", richText.plainText)
                        if (richText.linkUrl != null) putJsonObject("link") {
                            put("url", richText.linkUrl)
                        }
                    }

                    is UserMentionRichText -> putJsonObject("mention") {
                        putJsonObject("user") {
                            put("id", richText.user.id)
                        }
                    }

                    is PageMentionRichText -> putJsonObject("mention") {
                        putJsonObject("page") {
                            put("id", richText.pageId)
                        }
                    }

                    is DatabaseMentionRichText -> putJsonObject("mention") {
                        putJsonObject("database") {
                            put("id", richText.databaseId)
                        }
                    }

                    is DateMentionRichText -> putJsonObject("mention") {
                        putJsonObject("date") {
                            put("start", richText.dateOrDateRange.start.modelToApi(ApiDateStringConverter))
                            richText.dateOrDateRange.end?.let {
                                put("end", it.modelToApi(ApiDateStringConverter))
                            }
                        }
                    }

                    is EquationRichText -> putJsonObject("equation") {
                        put("expression", richText.expression)
                    }
                }
            }
        }
    }
}

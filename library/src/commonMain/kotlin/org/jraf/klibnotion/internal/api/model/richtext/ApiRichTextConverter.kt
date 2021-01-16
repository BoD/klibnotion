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

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.date.ApiDateConverter
import org.jraf.klibnotion.internal.api.model.user.ApiUserConverter
import org.jraf.klibnotion.internal.model.richtext.DatabaseMentionRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.DateMentionRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.EquationRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.PageMentionRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.TextRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.UnknownTypeMentionRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.UnknownTypeRichTextImpl
import org.jraf.klibnotion.internal.model.richtext.UserMentionRichTextImpl
import org.jraf.klibnotion.model.richtext.RichText

internal object ApiRichTextConverter : ApiConverter<ApiRichText, RichText>() {
    override fun apiToModel(apiModel: ApiRichText): RichText {
        val plainText = apiModel.plain_text
        val href = apiModel.href
        val annotations = ApiAnnotationsConverter.apiToModel(apiModel.annotations)
        return when (apiModel.type) {
            "text" -> TextRichTextImpl(
                plainText = plainText,
                href = href,
                annotations = annotations,
            )

            "mention" -> when (apiModel.mention!!.type) {
                "user" -> UserMentionRichTextImpl(
                    plainText = plainText,
                    href = href,
                    annotations = annotations,
                    user = ApiUserConverter.apiToModel(apiModel.mention.user!!)
                )
                "page" -> PageMentionRichTextImpl(
                    plainText = plainText,
                    href = href,
                    annotations = annotations,
                    pageId = apiModel.mention.page!!.id
                )
                "database" -> DatabaseMentionRichTextImpl(
                    plainText = plainText,
                    href = href,
                    annotations = annotations,
                    databaseId = apiModel.mention.database!!.id
                )
                "date" -> DateMentionRichTextImpl(
                    plainText = plainText,
                    href = href,
                    annotations = annotations,
                    dateOrDateRange = ApiDateConverter.apiToModel(apiModel.mention.date!!)
                )
                else -> UnknownTypeMentionRichTextImpl(
                    plainText = plainText,
                    href = href,
                    annotations = annotations,
                    type = apiModel.mention.type
                )
            }

            "equation" -> EquationRichTextImpl(
                plainText = plainText,
                href = href,
                annotations = annotations,
                expression = apiModel.equation!!.expression
            )

            else -> UnknownTypeRichTextImpl(
                plainText = plainText,
                href = href,
                annotations = annotations,
                type = apiModel.type
            )
        }
    }
}
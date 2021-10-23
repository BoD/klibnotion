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

package org.jraf.klibnotion.internal.api.model.property.value

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.apiToModel
import org.jraf.klibnotion.internal.api.model.base.ApiNumberConverter
import org.jraf.klibnotion.internal.api.model.date.ApiDateConverter
import org.jraf.klibnotion.internal.api.model.date.ApiDateStringConverter
import org.jraf.klibnotion.internal.api.model.property.ApiSelectOptionConverter
import org.jraf.klibnotion.internal.api.model.property.value.formula.ApiPropertyValueFormulaConverter
import org.jraf.klibnotion.internal.api.model.property.value.rollup.ApiPropertyValueRollupConverter
import org.jraf.klibnotion.internal.api.model.richtext.ApiRichTextConverter
import org.jraf.klibnotion.internal.api.model.user.ApiUserConverter
import org.jraf.klibnotion.internal.model.property.value.CheckboxPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.CreatedByPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.CreatedTimePropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.DatePropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.EmailPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.FilesPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.LastEditedByPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.LastEditedTimePropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.MultiSelectPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.NumberPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.PeoplePropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.PhoneNumberPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.RelationPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.RichTextPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.SelectPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.TitlePropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.UnknownTypePropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.UrlPropertyValueImpl
import org.jraf.klibnotion.model.property.value.PropertyValue
import org.jraf.klibnotion.model.richtext.RichTextList

internal object ApiPropertyValueConverter :
    ApiConverter<Pair<String, ApiPropertyValue>, PropertyValue<*>>() {
    override fun apiToModel(apiModel: Pair<String, ApiPropertyValue>): PropertyValue<*> {
        val (name, apiPropertyValue) = apiModel
        val id = apiPropertyValue.id
        return when (val type = apiPropertyValue.type) {
            "rich_text" -> RichTextPropertyValueImpl(
                id = id,
                name = name,
                value = RichTextList(apiPropertyValue.rich_text!!.apiToModel(ApiRichTextConverter))
            )
            "number" -> NumberPropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.number?.apiToModel(ApiNumberConverter)
            )
            "select" -> SelectPropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.select?.apiToModel(ApiSelectOptionConverter)
            )
            "multi_select" -> MultiSelectPropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.multi_select!!.apiToModel(ApiSelectOptionConverter)
            )
            "date" -> DatePropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.date?.apiToModel(ApiDateConverter)
            )
            "formula" -> ApiPropertyValueFormulaConverter.apiToModel(apiModel)
            "relation" -> RelationPropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.relation!!.map { it.id }
            )
            "rollup" -> ApiPropertyValueRollupConverter.apiToModel(apiModel)
            "title" -> TitlePropertyValueImpl(
                id = id,
                name = name,
                value = RichTextList(apiPropertyValue.title!!.apiToModel(ApiRichTextConverter))
            )
            "people" -> PeoplePropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.people!!.apiToModel(ApiUserConverter)
            )
            "files" -> FilesPropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.files!!.apiToModel(ApiPropertyValueFileConverter)
            )
            "checkbox" -> CheckboxPropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.checkbox!!
            )
            "url" -> UrlPropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.url
            )
            "email" -> EmailPropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.email
            )
            "phone_number" -> PhoneNumberPropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.phone_number
            )
            "created_time" -> CreatedTimePropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.created_time!!.apiToModel(ApiDateStringConverter)
            )
            "created_by" -> CreatedByPropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.created_by!!.apiToModel(ApiUserConverter)
            )
            "last_edited_time" -> LastEditedTimePropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.last_edited_time!!.apiToModel(ApiDateStringConverter)
            )
            "last_edited_by" -> LastEditedByPropertyValueImpl(
                id = id,
                name = name,
                value = apiPropertyValue.last_edited_by!!.apiToModel(ApiUserConverter)
            )
            else -> UnknownTypePropertyValueImpl(
                id = id,
                name = name,
                type = type,
            )
        }
    }
}
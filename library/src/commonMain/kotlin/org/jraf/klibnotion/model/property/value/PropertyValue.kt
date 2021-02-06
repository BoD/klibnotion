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

package org.jraf.klibnotion.model.property.value

import org.jraf.klibnotion.internal.model.property.SelectOptionImpl
import org.jraf.klibnotion.internal.model.property.value.DatePropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.MultiSelectPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.NumberPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.RelationPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.SelectPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.TextPropertyValueImpl
import org.jraf.klibnotion.internal.model.richtext.AnnotationsImpl
import org.jraf.klibnotion.internal.model.richtext.RichTextListImpl
import org.jraf.klibnotion.internal.model.richtext.TextRichTextImpl
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.color.Color
import org.jraf.klibnotion.model.date.DateOrDateRange

/**
 * See [https://www.notion.so/5a48631ae00c4d48adee859475a25956?v=5dfe884a62304ae08f1fb7d0e89c5743].
 */
interface PropertyValue<T : Any> {
    val id: String
    val name: String
    val value: T

    companion object {
        fun numberProperty(idOrName: String, value: Number): NumberPropertyValue = NumberPropertyValueImpl(
            id = idOrName,
            name = idOrName,
            value = value
        )

        fun textProperty(idOrName: String, value: String): TextPropertyValue = TextPropertyValueImpl(
            id = idOrName,
            name = idOrName,
            value = RichTextListImpl(
                listOf(
                    TextRichTextImpl(
                        plainText = value,
                        href = null,
                        annotations = AnnotationsImpl(
                            bold = false,
                            italic = false,
                            strikethrough = false,
                            underline = false,
                            code = false,
                            color = Color.DEFAULT
                        )
                    )
                )
            )
        )

        fun selectPropertyByName(idOrName: String, selectName: String): SelectPropertyValue = SelectPropertyValueImpl(
            id = idOrName,
            name = idOrName,
            value = SelectOptionImpl(id = "", name = selectName, color = Color.DEFAULT)
        )

        // Actually not supported by the Notion API for now. Keeping it commented out for now because it may be supported
        // in a future version.
        //        fun selectPropertyById(idOrName: String, selectId: UuidString): SelectPropertyValue = SelectPropertyValueImpl(
        //            id = idOrName,
        //            name = idOrName,
        //            value = SelectOptionImpl(id = selectId, name = "", color = Color.DEFAULT)
        //        )

        fun multiSelectPropertyByName(idOrName: String, vararg selectNames: String): MultiSelectPropertyValue =
            MultiSelectPropertyValueImpl(
                id = idOrName,
                name = idOrName,
                value = selectNames.map { selectName ->
                    SelectOptionImpl(
                        id = "",
                        name = selectName,
                        color = Color.DEFAULT
                    )
                }
            )

        fun dateProperty(idOrName: String, date: DateOrDateRange): DatePropertyValue =
            DatePropertyValueImpl(
                id = idOrName,
                name = idOrName,
                value = date,
            )

        fun relationProperty(idOrName: String, vararg pageIds: UuidString): RelationPropertyValue =
            RelationPropertyValueImpl(
                id = idOrName,
                name = idOrName,
                value = pageIds.asList(),
            )
    }
}

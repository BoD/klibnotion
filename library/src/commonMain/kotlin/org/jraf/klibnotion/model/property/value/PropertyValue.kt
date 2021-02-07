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
import org.jraf.klibnotion.internal.model.property.value.CheckboxPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.DatePropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.MultiSelectPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.NumberPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.PeoplePropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.RelationPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.SelectPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.StringPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.TextPropertyValueImpl
import org.jraf.klibnotion.internal.model.richtext.AnnotationsImpl
import org.jraf.klibnotion.internal.model.richtext.RichTextListImpl
import org.jraf.klibnotion.internal.model.richtext.TextRichTextImpl
import org.jraf.klibnotion.internal.model.user.PersonImpl
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
}

class PropertyValueList {
    internal val propertyValueList = mutableListOf<PropertyValue<*>>()

    private fun add(propertyValue: PropertyValue<*>): PropertyValueList {
        propertyValueList.add(propertyValue)
        return this
    }

    fun number(idOrName: String, value: Number): PropertyValueList = add(NumberPropertyValueImpl(
        id = idOrName,
        name = idOrName,
        value = value
    ))

    fun text(idOrName: String, value: String): PropertyValueList = add(TextPropertyValueImpl(
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
    ))

    fun selectByName(idOrName: String, selectName: String): PropertyValueList = add(SelectPropertyValueImpl(
        id = idOrName,
        name = idOrName,
        value = SelectOptionImpl(id = "", name = selectName, color = Color.DEFAULT)
    ))

    // Actually not supported by the Notion API for now. Keeping it commented out for now because it may be supported
    // in a future version.
    //        fun selectById(idOrName: String, selectId: UuidString): PropertyValueList = add(SelectPropertyValueImpl(
    //            id = idOrName,
    //            name = idOrName,
    //            value = SelectOptionImpl(id = selectId, name = "", color = Color.DEFAULT)
    //        ))

    fun multiSelectByNames(idOrName: String, vararg selectNames: String): PropertyValueList =
        add(MultiSelectPropertyValueImpl(
            id = idOrName,
            name = idOrName,
            value = selectNames.map { selectName ->
                SelectOptionImpl(
                    id = "",
                    name = selectName,
                    color = Color.DEFAULT
                )
            }
        ))

    fun date(idOrName: String, date: DateOrDateRange): PropertyValueList = add(DatePropertyValueImpl(
        id = idOrName,
        name = idOrName,
        value = date,
    ))

    fun relation(idOrName: String, vararg pageIds: UuidString): PropertyValueList = add(RelationPropertyValueImpl(
        id = idOrName,
        name = idOrName,
        value = pageIds.asList(),
    ))

    fun people(idOrName: String, vararg peopleIds: UuidString): PropertyValueList = add(PeoplePropertyValueImpl(
        id = idOrName,
        name = idOrName,
        value = peopleIds.map { PersonImpl(it, "", null, "") },
    ))

    fun checkbox(idOrName: String, value: Boolean): PropertyValueList = add(CheckboxPropertyValueImpl(
        id = idOrName,
        name = idOrName,
        value = value,
    ))

    /**
     * Can be used for Url, Email and Phone number properties.
     */
    fun string(idOrName: String, value: String): PropertyValueList = add(StringPropertyValueImpl(
        id = idOrName,
        name = idOrName,
        value = value,
    ))
}

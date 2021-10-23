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
import org.jraf.klibnotion.internal.model.property.value.EmailPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.MultiSelectPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.NumberPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.PeoplePropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.PhoneNumberPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.RelationPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.RichTextPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.SelectPropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.TitlePropertyValueImpl
import org.jraf.klibnotion.internal.model.property.value.UrlPropertyValueImpl
import org.jraf.klibnotion.internal.model.user.PersonImpl
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.color.Color
import org.jraf.klibnotion.model.date.DateOrDateRange
import org.jraf.klibnotion.model.richtext.Annotations
import org.jraf.klibnotion.model.richtext.RichTextList
import kotlin.jvm.JvmOverloads

/**
 * See [Reference](https://developers.notion.com/reference/page#all-property-values).
 */
sealed interface PropertyValue<T> {
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

    fun number(idOrName: String, number: Number?): PropertyValueList = add(NumberPropertyValueImpl(
        id = idOrName,
        name = idOrName,
        value = number
    ))

    @JvmOverloads
    fun text(
        idOrName: String,
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): PropertyValueList = text(idOrName = idOrName, richTextList = RichTextList().text(text, linkUrl, annotations))

    fun text(idOrName: String, richTextList: RichTextList?): PropertyValueList = add(
        RichTextPropertyValueImpl(
            id = idOrName,
            name = idOrName,
            value = richTextList ?: RichTextList(),
        )
    )

    @JvmOverloads
    fun title(
        idOrName: String,
        text: String,
        linkUrl: String? = null,
        annotations: Annotations = Annotations.DEFAULT,
    ): PropertyValueList = title(idOrName = idOrName, richTextList = RichTextList().text(text, linkUrl, annotations))

    fun title(idOrName: String, richTextList: RichTextList?): PropertyValueList = add(
        TitlePropertyValueImpl(
            id = idOrName,
            name = idOrName,
            value = richTextList ?: RichTextList(),
        )
    )

    fun selectByName(idOrName: String, selectName: String?): PropertyValueList = add(
        SelectPropertyValueImpl(
            id = idOrName,
            name = idOrName,
            value = selectName?.let { SelectOptionImpl(id = "", name = it, color = Color.DEFAULT) }
        )
    )

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

    fun date(idOrName: String, date: DateOrDateRange?): PropertyValueList = add(DatePropertyValueImpl(
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

    fun checkbox(idOrName: String, checked: Boolean): PropertyValueList = add(
        CheckboxPropertyValueImpl(
            id = idOrName,
            name = idOrName,
            value = checked,
        )
    )

    fun url(idOrName: String, url: String?): PropertyValueList = add(
        UrlPropertyValueImpl(
            id = idOrName,
            name = idOrName,
            value = url,
        )
    )

    fun email(idOrName: String, email: String?): PropertyValueList = add(
        EmailPropertyValueImpl(
            id = idOrName,
            name = idOrName,
            value = email,
        )
    )

    fun phoneNumber(idOrName: String, phoneNumber: String?): PropertyValueList = add(
        PhoneNumberPropertyValueImpl(
            id = idOrName,
            name = idOrName,
            value = phoneNumber,
        )
    )
}

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

package org.jraf.klibnotion.model.property.spec

import org.jraf.klibnotion.internal.model.property.spec.CheckboxPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.CreatedByPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.CreatedTimePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.DatePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.EmailPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.FilesPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.LastEditedByPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.LastEditedTimePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.MultiSelectPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.NumberPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.PeoplePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.PhoneNumberPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.RichTextPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.SelectPropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.TitlePropertySpecImpl
import org.jraf.klibnotion.internal.model.property.spec.UrlPropertySpecImpl
import org.jraf.klibnotion.model.property.SelectOptionList

/**
 * See [Reference](https://developers.notion.com/reference/database).
 */
sealed interface PropertySpec {
    /**
     * The name of the property as it appears in Notion.
     */
    val name: String

    /**
     * The ID of the property, usually a short string of random letters and symbols.
     * Some automatically generated property types have special human-readable IDs.
     * For example, all Title properties have an ID of `title`.
     */
    val id: String
}

class PropertySpecList {
    internal val propertySpecList = mutableListOf<PropertySpec>()

    private fun add(propertySpec: PropertySpec): PropertySpecList {
        propertySpecList.add(propertySpec)
        return this
    }

    fun number(name: String, format: NumberPropertySpec.NumberFormat): PropertySpecList = add(
        NumberPropertySpecImpl(
            id = name,
            name = name,
            format = format,
        )
    )

    fun text(name: String): PropertySpecList = add(
        RichTextPropertySpecImpl(
            id = name,
            name = name,
        )
    )

    fun title(name: String): PropertySpecList = add(
        TitlePropertySpecImpl(
            id = name,
            name = name,
        )
    )

    fun select(name: String, options: SelectOptionList): PropertySpecList = add(
        SelectPropertySpecImpl(
            id = name,
            name = name,
            options = options.selectOptionList,
        )
    )

    fun multiSelect(name: String, options: SelectOptionList): PropertySpecList = add(
        MultiSelectPropertySpecImpl(
            id = name,
            name = name,
            options = options.selectOptionList,
        )
    )

    fun date(name: String): PropertySpecList = add(
        DatePropertySpecImpl(
            id = name,
            name = name,
        )
    )

    // Commented for now because for some reason this is not supported by the API at the moment
//    fun relation(
//        name: String,
//        databaseId: UuidString,
//        syncedPropertyName: String,
//        syncedPropertyId: String,
//    ): PropertySpecList = add(
//        RelationPropertySpecImpl(
//            id = name,
//            name = name,
//            databaseId = databaseId,
//            syncedPropertyName = syncedPropertyName,
//            syncedPropertyId = syncedPropertyId,
//        )
//    )

    fun people(name: String): PropertySpecList = add(
        PeoplePropertySpecImpl(
            id = name,
            name = name,
        )
    )

    fun checkbox(name: String): PropertySpecList = add(
        CheckboxPropertySpecImpl(
            id = name,
            name = name,
        )
    )

    fun url(name: String): PropertySpecList = add(
        UrlPropertySpecImpl(
            id = name,
            name = name,
        )
    )

    fun email(name: String): PropertySpecList = add(
        EmailPropertySpecImpl(
            id = name,
            name = name,
        )
    )

    fun phoneNumber(name: String): PropertySpecList = add(
        PhoneNumberPropertySpecImpl(
            id = name,
            name = name,
        )
    )

    fun file(name: String): PropertySpecList = add(
        FilesPropertySpecImpl(
            id = name,
            name = name,
        )
    )

    // Commented for now because for some reason this is not supported by the API at the moment
//    fun formula(name: String, expression: String): PropertySpecList = add(
//        FormulaPropertySpecImpl(
//            id = name,
//            name = name,
//            expression = expression,
//        )
//    )

    fun createdTime(name: String): PropertySpecList = add(
        CreatedTimePropertySpecImpl(
            id = name,
            name = name,
        )
    )

    fun createdBy(name: String): PropertySpecList = add(
        CreatedByPropertySpecImpl(
            id = name,
            name = name,
        )
    )

    fun lastEditedTime(name: String): PropertySpecList = add(
        LastEditedTimePropertySpecImpl(
            id = name,
            name = name,
        )
    )

    fun lastEditedBy(name: String): PropertySpecList = add(
        LastEditedByPropertySpecImpl(
            id = name,
            name = name,
        )
    )

    // Commented for now because for some reason this is not supported by the API at the moment
//    fun rollup(
//        name: String,
//        relationPropertyName: String,
//        relationPropertyId: String,
//        rollupPropertyName: String,
//        rollupPropertyId: String,
//        function: RollupPropertySpec.RollupFunction,
//    ): PropertySpecList = add(
//        RollupPropertySpecImpl(
//            id = name,
//            name = name,
//            relationPropertyName = relationPropertyName,
//            relationPropertyId = relationPropertyId,
//            rollupPropertyName = rollupPropertyName,
//            rollupPropertyId = rollupPropertyId,
//            function = function,
//        )
//    )
}

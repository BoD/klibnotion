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

package org.jraf.klibnotion.model.property

import org.jraf.klibnotion.internal.model.property.SelectOptionImpl
import org.jraf.klibnotion.model.color.Color

interface SelectOption {
    /**
     * Name of the option as it appears in Notion.
     */
    val name: String

    /**
     * ID of the option, which does not change if the name is changed. These are sometimes, but not always, UUIDs.
     */
    val id: String

    /**
     * Color of the option.
     */
    val color: Color
}

class SelectOptionList {
    internal val selectOptionList = mutableListOf<SelectOption>()

    private fun add(selectOption: SelectOption): SelectOptionList {
        selectOptionList.add(selectOption)
        return this
    }

    fun option(name: String, color: Color) = add(SelectOptionImpl(name = name, id = name, color = color))
}

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

package org.jraf.klibnotion.model.database.query

class DatabaseQuerySort() {
    internal val sorting: MutableList<Pair<String, Direction>> = mutableListOf()

    @Deprecated("Use ascending and descending methods instead")
    constructor(propertyName: String, direction: Direction) : this() {
        sorting += propertyName to direction
    }

    @Deprecated("Use ascending and descending methods instead")
    fun add(propertyName: String, direction: Direction): DatabaseQuerySort {
        sorting += propertyName to direction
        return this
    }

    fun ascending(propertyName: String) = add(propertyName, direction = Direction.ASCENDING)
    fun descending(propertyName: String) = add(propertyName, direction = Direction.DESCENDING)

    @Deprecated("Use ascending and descending methods")
    /* internal */ enum class Direction {
        ASCENDING,
        DESCENDING,
    }
}

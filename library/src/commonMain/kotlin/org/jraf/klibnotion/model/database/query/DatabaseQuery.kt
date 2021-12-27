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

package org.jraf.klibnotion.model.database.query

import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPropertyFilter

class DatabaseQuery {
    internal val allFilters = mutableSetOf<DatabaseQueryPropertyFilter>()
    internal val anyFilters = mutableSetOf<DatabaseQueryPropertyFilter>()

    fun all(vararg filter: DatabaseQueryPropertyFilter): DatabaseQuery {
        allFilters += filter
        return this
    }

    fun any(vararg filter: DatabaseQueryPropertyFilter): DatabaseQuery {
        anyFilters += filter
        return this
    }

    @Deprecated("Use all method instead", replaceWith = ReplaceWith("this.all(*filter)"))
    fun addAllFilters(vararg filter: DatabaseQueryPropertyFilter) = all(*filter)

    @Deprecated("Use any method instead", replaceWith = ReplaceWith("this.any(*filter)"))
    fun addAnyFilters(vararg filter: DatabaseQueryPropertyFilter) = any(*filter)

    override fun toString(): String {
        return "DatabaseQueryImpl(allFilters=$allFilters, anyFilters=$anyFilters)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DatabaseQuery

        if (allFilters != other.allFilters) return false
        if (anyFilters != other.anyFilters) return false

        return true
    }

    override fun hashCode(): Int {
        var result = allFilters.hashCode()
        result = 31 * result + anyFilters.hashCode()
        return result
    }
}

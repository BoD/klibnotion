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

package org.jraf.klibnotion.internal.api.model.database.query

import kotlinx.serialization.Serializable

/**
 * See [https://www.notion.so/Query-a-database-f150c8e10ead4afe9045d56bc8114855].
 */
@Serializable
internal data class ApiDatabaseQueryFilters(
    val or: List<ApiDatabaseQueryFilter>? = null,
    val and: List<ApiDatabaseQueryFilter>? = null,
)


/**
 * See [https://www.notion.so/Query-a-database-f150c8e10ead4afe9045d56bc8114855].
 */
@Serializable
internal data class ApiDatabaseQueryFilter(
    val property: String,
    val title: ApiTextDatabaseQueryFilter? = null,
    val text: ApiTextDatabaseQueryFilter? = null,
    val number: ApiNumberDatabaseQueryFilter? = null,
    val checkbox: ApiCheckboxDatabaseQueryFilter? = null,
    val select: ApiSelectDatabaseQueryFilter? = null,
    val multi_select: ApiMultiSelectDatabaseQueryFilter? = null,
    val date: ApiDateDatabaseQueryFilter? = null,
    val people: ApiPeopleDatabaseQueryFilter? = null,
    val files: ApiFilesDatabaseQueryFilter? = null,
    val url: ApiTextDatabaseQueryFilter? = null,
    val email: ApiTextDatabaseQueryFilter? = null,
    val phone_number: ApiTextDatabaseQueryFilter? = null,
    val relation: ApiRelationDatabaseQueryFilter? = null,
    val formula: ApiFormulaDatabaseQueryFilter? = null,
    val created_by: ApiPeopleDatabaseQueryFilter? = null,
    val created_time: ApiDateDatabaseQueryFilter? = null,
    val last_edited_by: ApiPeopleDatabaseQueryFilter? = null,
    val last_edited_time: ApiDateDatabaseQueryFilter? = null,
)

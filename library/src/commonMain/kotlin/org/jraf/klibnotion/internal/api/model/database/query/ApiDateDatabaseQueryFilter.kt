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
internal data class ApiDateDatabaseQueryFilter(
    val equals: String? = null,
    val before: String? = null,
    val after: String? = null,
    val on_or_before: String? = null,
    val on_or_after: String? = null,
    val past_week: Map<String, String>? = null,
    val past_month: Map<String, String>? = null,
    val past_year: Map<String, String>? = null,
    val next_week: Map<String, String>? = null,
    val next_month: Map<String, String>? = null,
    val next_year: Map<String, String>? = null,
    val is_empty: Boolean? = null,
    val is_not_empty: Boolean? = null,
)

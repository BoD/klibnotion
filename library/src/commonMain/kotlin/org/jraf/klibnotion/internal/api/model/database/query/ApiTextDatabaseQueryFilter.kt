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
 * See [Reference](https://developers.notion.com/reference/post-database-query).
 */
@Serializable
internal data class ApiTextDatabaseQueryFilter(
    val equals: String? = null,
    val does_not_equal: String? = null,
    val contains: String? = null,
    val does_not_contain: String? = null,
    val starts_with: String? = null,
    val ends_with: String? = null,
    val is_empty: Boolean? = null,
    val is_not_empty: Boolean? = null,
)

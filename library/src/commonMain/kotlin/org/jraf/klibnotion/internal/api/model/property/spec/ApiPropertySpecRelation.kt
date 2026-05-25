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

package org.jraf.klibnotion.internal.api.model.property.spec

import kotlinx.serialization.Serializable

/**
 * See [Reference](https://developers.notion.com/reference/database).
 *
 * As of API version 2022-06-28, the relation type is either `single_property` (no synced
 * property) or `dual_property` (synced property info lives inside the nested object).
 */
@Serializable
internal data class ApiPropertySpecRelation(
    val database_id: String,
    val type: String? = null,
    val single_property: ApiEmpty? = null,
    val dual_property: ApiPropertySpecRelationDualProperty? = null,
)

@Serializable
internal data class ApiPropertySpecRelationDualProperty(
    val synced_property_name: String? = null,
    val synced_property_id: String? = null,
)

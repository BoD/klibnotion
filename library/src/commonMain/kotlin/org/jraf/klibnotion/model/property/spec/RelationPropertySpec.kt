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

import org.jraf.klibnotion.model.base.UuidString

/**
 * See [Reference](https://developers.notion.com/reference/database).
 */
interface RelationPropertySpec : PropertySpec {
    /**
     * The database this relation refers to.
     * New linked pages must belong to this database in order to be valid.
     */
    val databaseId: UuidString

    /**
     * By default, relations are formed as two synced properties across databases: if you make a change to one property,
     * it updates the synced property at the same time.
     * [syncedPropertyName] refers to the name of the property in the related database.
     */
    val syncedPropertyName: String

    /**
     * By default, relations are formed as two synced properties across databases: if you make a change to one property,
     * it updates the synced property at the same time.
     * [syncedPropertyId] refers to the id of the property in the related database.
     * Like [PropertySpec.id], this is usually a short string of random letters and symbols.
     */
    val syncedPropertyId: String
}
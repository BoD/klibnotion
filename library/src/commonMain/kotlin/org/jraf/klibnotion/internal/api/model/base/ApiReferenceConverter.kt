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

package org.jraf.klibnotion.internal.api.model.base

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.model.base.reference.DatabaseReference
import org.jraf.klibnotion.model.base.reference.PageReference
import org.jraf.klibnotion.model.base.reference.Reference
import org.jraf.klibnotion.model.base.reference.UnknownTypeReference

internal object ApiReferenceConverter : ApiConverter<ApiReference, Reference>() {
    override fun apiToModel(apiModel: ApiReference) = when (val type = apiModel.type) {
        "database_id" -> DatabaseReference(apiModel.database_id!!)
        "page_id" -> PageReference(apiModel.page_id!!)
        else -> UnknownTypeReference(type = type, id = "(unknown)")
    }

    override fun modelToApi(model: Reference) = when (model) {
        is DatabaseReference -> ApiReference(type = "database_id", database_id = model.id)
        is PageReference -> ApiReference(type = "page_id", page_id = model.id)
        is UnknownTypeReference -> throw IllegalStateException()
    }
}
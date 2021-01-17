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

package org.jraf.klibnotion.internal.api.model.page

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.property.value.ApiPropertyValueConverter
import org.jraf.klibnotion.internal.model.base.reference.DatabaseReferenceImpl
import org.jraf.klibnotion.internal.model.base.reference.UnknownTypeReferenceImpl
import org.jraf.klibnotion.internal.model.page.PageImpl
import org.jraf.klibnotion.model.page.Page

internal object ApiPageConverter : ApiConverter<ApiPage, Page>() {
    override fun apiToModel(apiModel: ApiPage) = PageImpl(
        id = apiModel.id,
        parent = when (val type = apiModel.parent.type) {
            "database_id" -> DatabaseReferenceImpl(apiModel.parent.database_id!!)
            else -> UnknownTypeReferenceImpl(type = type, id = "(unknown)")
        },
        propertyValues = ApiPropertyValueConverter.apiToModel(apiModel.properties.map { it.key to it.value }),
        archived = apiModel.archived
    )
}
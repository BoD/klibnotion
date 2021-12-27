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

package org.jraf.klibnotion.internal.api.model.page

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.apiToModel
import org.jraf.klibnotion.internal.api.model.base.ApiEmojiOrFileConverter
import org.jraf.klibnotion.internal.api.model.base.ApiReferenceConverter
import org.jraf.klibnotion.internal.api.model.date.ApiDateStringConverter
import org.jraf.klibnotion.internal.api.model.property.value.ApiPropertyValueConverter
import org.jraf.klibnotion.internal.model.page.PageImpl
import org.jraf.klibnotion.model.file.File
import org.jraf.klibnotion.model.page.Page

internal object ApiPageConverter : ApiConverter<ApiPage, Page>() {
    override fun apiToModel(apiModel: ApiPage) = PageImpl(
        id = apiModel.id,
        parent = apiModel.parent.apiToModel(ApiReferenceConverter),
        propertyValues = ApiPropertyValueConverter.apiToModel(apiModel.properties.map { it.key to it.value }),
        archived = apiModel.archived,
        created = apiModel.created_time.apiToModel(ApiDateStringConverter).timestamp,
        lastEdited = apiModel.last_edited_time.apiToModel(ApiDateStringConverter).timestamp,
        url = apiModel.url,
        icon = apiModel.icon.apiToModel(ApiEmojiOrFileConverter),
        cover = apiModel.cover.apiToModel(ApiEmojiOrFileConverter) as? File,
    )
}

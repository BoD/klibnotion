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

package org.jraf.klibnotion.internal.api.model.property

import org.jraf.klibnotion.internal.api.model.ApiConverter
import org.jraf.klibnotion.internal.api.model.apiToModel
import org.jraf.klibnotion.internal.api.model.color.ApiColorConverter
import org.jraf.klibnotion.internal.api.model.modelToApi
import org.jraf.klibnotion.internal.model.property.SelectOptionImpl
import org.jraf.klibnotion.model.property.SelectOption

internal object ApiSelectOptionConverter : ApiConverter<ApiSelectOption, SelectOption>() {
    override fun apiToModel(apiModel: ApiSelectOption) =
        SelectOptionImpl(
            name = apiModel.name,
            id = apiModel.id!!,
            color = apiModel.color.apiToModel(ApiColorConverter)
        )

    override fun modelToApi(model: SelectOption): ApiSelectOption {
        return ApiSelectOption(
            name = model.name,
            color = model.color.modelToApi(ApiColorConverter)
        )
    }
}

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

package org.jraf.klibnotion.internal.api.model

internal abstract class ApiConverter<API_MODEL, MODEL> {
    open fun apiToModel(apiModel: API_MODEL): MODEL = throw NotImplementedError()

    open fun apiToModel(apiModelList: List<API_MODEL>): List<MODEL> =
        apiModelList.map { apiToModel(it) }

    open fun modelToApi(model: MODEL): API_MODEL = throw NotImplementedError()

    open fun modelToApi(modelList: List<MODEL>): List<API_MODEL> = modelList.map { modelToApi(it) }
}

internal fun <API_MODEL, MODEL> API_MODEL.apiToModel(converter: ApiConverter<API_MODEL, MODEL>) =
    converter.apiToModel(this)

internal fun <API_MODEL, MODEL> List<API_MODEL>.apiToModel(converter: ApiConverter<API_MODEL, MODEL>) =
    converter.apiToModel(this)

internal fun <API_MODEL, MODEL> MODEL.modelToApi(converter: ApiConverter<API_MODEL, MODEL>) =
    converter.modelToApi(this)

internal fun <API_MODEL, MODEL> List<MODEL>.modelToApi(converter: ApiConverter<API_MODEL, MODEL>) =
    converter.modelToApi(this)

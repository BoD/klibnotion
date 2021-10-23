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

package org.jraf.klibnotion.model.property.value

import org.jraf.klibnotion.model.date.DateOrDateRange

/**
 * Note: unlike [PropertyValue], the value of a [FormulaPropertyValue] *can* be `null`, because
 * a formula can for instance reference another property, which is unset.
 *
 * See [Reference](https://developers.notion.com/reference/page#all-property-values).
 */
sealed interface FormulaPropertyValue<T> : PropertyValue<T>

/**
 * See [Reference](https://developers.notion.com/reference/page#all-property-values).
 */
interface BooleanFormulaPropertyValue : FormulaPropertyValue<Boolean>

/**
 * See [Reference](https://developers.notion.com/reference/page#all-property-values).
 */
interface DateFormulaPropertyValue : FormulaPropertyValue<DateOrDateRange?>

/**
 * See [Reference](https://developers.notion.com/reference/page#all-property-values).
 */
interface NumberFormulaPropertyValue : FormulaPropertyValue<Number?>

/**
 * See [Reference](https://developers.notion.com/reference/page#all-property-values).
 */
interface StringFormulaPropertyValue : FormulaPropertyValue<String?>

/**
 * This type is returned when a Formula Property Value of a type unknown to this library is returned by the Notion API.
 *
 * See [Reference](https://developers.notion.com/reference/page#all-property-values).
 */
interface UnknownTypeFormulaPropertyValue : FormulaPropertyValue<Nothing?> {
    val type: String
}
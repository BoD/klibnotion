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

@file:OptIn(ExperimentalCli::class)

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.required
import kotlinx.cli.vararg

@Suppress("PropertyName", "PrivatePropertyName")
class Arguments(av: Array<String>) {
    private val parser = ArgParser("notion-cli")

    private val `api-key`: String by parser.option(ArgType.String, shortName = "k", description = "API key").required()
    val apiKey get() = `api-key`

    class User : Subcommand("user", "Get user information") {
        val userIds: List<String> by argument(ArgType.String, "User id(s)", description = "Id(s) of the user").vararg()

        override fun execute() {}
    }

    val user = User()

    init {
        parser.subcommands(user)
        parser.parse(av)
    }
}
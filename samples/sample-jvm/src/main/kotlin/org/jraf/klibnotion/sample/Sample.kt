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

package org.jraf.klibnotion.sample

import kotlinx.coroutines.runBlocking
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.HttpConfiguration
import org.jraf.klibnotion.client.HttpLoggingLevel
import org.jraf.klibnotion.client.HttpProxy
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.model.database.Database
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPredicate
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPropertyFilter
import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.SelectOption
import org.jraf.klibnotion.model.richtext.RichTextList
import org.jraf.klibnotion.model.user.User
import kotlin.system.exitProcess

// !!!!! DO THIS FIRST !!!!!
// Replace this constant with your API key
// that you will find by following the instructions here: https://www.notion.so/Getting-started-da32a6fc1bcc4403a6126ee735710d89
private const val API_KEY = "secret_XXX"

// Replace this constant with a user id that exists
private const val USER_ID = "00000000-0000-0000-0000-000000000000"

// Replace this constant with a database id that exists
private const val DATABASE_ID = "00000000000000000000"

class Sample {
    private val client: NotionClient by lazy {
        // Create the client
        NotionClient.newInstance(
            ClientConfiguration(
                Authentication(API_KEY),
                HttpConfiguration(
                    // Uncomment to see more logs
                    // loggingLevel = HttpLoggingLevel.BODY,
                    loggingLevel = HttpLoggingLevel.INFO,
                    // This is only needed to debug with, e.g., Charles Proxy
                    httpProxy = HttpProxy("localhost", 8888)
                )
            )
        )
    }

    fun main() {
        runBlocking {
            // Get user
            println("User:")
            val user: User = client.users.getUser(USER_ID)
            println(user)

            // Get user list
            println("User list first page:")
            val userResultPage: ResultPage<User> = client.users.getUserList()
            println(userResultPage)

            // Get database
            println("Database:")
            val database: Database = client.databases.getDatabase(DATABASE_ID)
            println(database)
            println("title=${database.title.plainText}")

            // Query database (simple)
            println("Simple query results:")
            val simpleQueryResultPage: ResultPage<Page> =
                client.databases.queryDatabase(DATABASE_ID)
            println(simpleQueryResultPage.results.joinToString("") { it.toFormattedString() })

            // Query database (filters)
            println("Filtered query results:")
            val filteredQueryResultPage: ResultPage<Page> = client.databases.queryDatabase(
                DATABASE_ID,
                query = DatabaseQuery.newInstance()
                    .addAnyFilters(
                        DatabaseQueryPropertyFilter.Text(
                            propertyIdOrName = "Famous quote",
                            predicate = DatabaseQueryPredicate.Text.Equals("a")
                        ),
                        DatabaseQueryPropertyFilter.Text(
                            propertyIdOrName = "Famous quote",
                            predicate = DatabaseQueryPredicate.Text.Contains("imp")
                        ),
                        DatabaseQueryPropertyFilter.Number(
                            propertyIdOrName = "Legs",
                            predicate = DatabaseQueryPredicate.Number.GreaterThanOrEqualTo(4)
                        ),
                        DatabaseQueryPropertyFilter.Formula(
                            propertyIdOrName = "Legs plus one",
                            predicate = DatabaseQueryPredicate.Formula.Number.GreaterThan(4)
                        ),
                        DatabaseQueryPropertyFilter.Checkbox(
                            propertyIdOrName = "Is Greedo",
                            predicate = DatabaseQueryPredicate.Checkbox(true)
                        ),
                    )
            )
            println(filteredQueryResultPage.results.joinToString("") { it.toFormattedString() })
        }

        // Close
        client.close()

        // Exit process
        exitProcess(0)
    }

    private fun Page.toFormattedString(): String {
        val res = StringBuilder("-----------\n")
        res.append("Id: $id\n")
        for (propertyValue in propertyValues) {
            res.append("${propertyValue.name}: ${propertyValue.value.toFormattedString()}\n")
        }
        return res.toString()
    }

    private fun <T> T.toFormattedString(): String {
        return when (this) {
            is RichTextList -> plainText ?: ""
            is SelectOption -> name
            else -> toString()
        }
    }
}

fun main() = Sample().main()

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

import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.model.user.AnonymousUser
import org.jraf.klibnotion.model.user.Bot
import org.jraf.klibnotion.model.user.Person
import org.jraf.klibnotion.model.user.UnknownTypeUser
import org.jraf.klibnotion.model.user.User

class NotionCli(av: Array<String>) {
    private val arguments = Arguments(av)

    private val client =
        NotionClient.newInstance(ClientConfiguration(Authentication(arguments.internalIntegrationToken)))

    suspend fun main() {
        for ((index, userId) in arguments.user.userIds.withIndex()) {
            val user: User = client.users.getUser(userId)
            println(user.toNiceString())
            if (index != arguments.user.userIds.lastIndex) {
                println()
            }
        }

        // Close
        client.close()
    }
}

private fun User.toNiceString(): String {
    return """
    Id: $id
    Type: ${
        when (this) {
            is Person -> "Person"
            is Bot -> "Bot"
            is AnonymousUser -> "Anonymous"
            is UnknownTypeUser -> "Unknown"
        }
    }
    Name: ${
        when (this) {
            is Person -> name
            is Bot -> name
            is AnonymousUser -> ""
            is UnknownTypeUser -> ""
        }
    }

    Avatar url: $avatarUrl
    """.trimIndent() +
            if (this is Person) "\nEmail: $email" else ""
}

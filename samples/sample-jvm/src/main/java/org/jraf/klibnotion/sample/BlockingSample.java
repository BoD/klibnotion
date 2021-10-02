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

package org.jraf.klibnotion.sample;

import kotlin.Unit;
import org.jraf.klibnotion.client.*;
import org.jraf.klibnotion.client.blocking.BlockingNotionClient;
import org.jraf.klibnotion.client.blocking.BlockingNotionClientUtils;
import org.jraf.klibnotion.model.base.reference.DatabaseReference;
import org.jraf.klibnotion.model.color.Color;
import org.jraf.klibnotion.model.database.Database;
import org.jraf.klibnotion.model.database.query.DatabaseQuery;
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPredicate;
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPropertyFilter;
import org.jraf.klibnotion.model.date.Date;
import org.jraf.klibnotion.model.date.DateOrDateRange;
import org.jraf.klibnotion.model.date.DateTime;
import org.jraf.klibnotion.model.page.Page;
import org.jraf.klibnotion.model.pagination.Pagination;
import org.jraf.klibnotion.model.pagination.ResultPage;
import org.jraf.klibnotion.model.property.sort.PropertySort;
import org.jraf.klibnotion.model.property.value.PropertyValueList;
import org.jraf.klibnotion.model.richtext.Annotations;
import org.jraf.klibnotion.model.richtext.RichTextList;
import org.jraf.klibnotion.model.user.User;

import java.util.Random;

import static org.jraf.klibnotion.model.emoji.EmojiKt.Emoji;
import static org.jraf.klibnotion.model.file.FileKt.File;

class BlockingSample {
    // !!!!! DO THIS FIRST !!!!!
    // Replace this constant with your Internal Integration Token
    // that you will find by following the instructions here: https://developers.notion.com/docs/getting-started
    private static final String TOKEN = "secret_XXX";

    // Replace this constant with a user id that exists
    private static final String USER_ID = "00000000-0000-0000-0000-000000000000";

    // Replace this constant with a database id that exists
    private static final String DATABASE_ID = "00000000-0000-0000-0000-000000000000";

    // Replace this constant with a page id that exists
    private static final String PAGE_ID = "00000000-0000-0000-0000-000000000000";

    private BlockingNotionClient client;

    private void initClient() {
        NotionClient notionClient = NotionClient.newInstance(
                new ClientConfiguration(
                        new Authentication(TOKEN),
                        new HttpConfiguration(
                                // Uncomment to see more logs
                                // loggingLevel = HttpLoggingLevel.BODY,
                                HttpLoggingLevel.INFO,
                                null,
                                // This is only needed to debug with, e.g., Charles Proxy
                                new HttpProxy("localhost", 8888),
                                true
                        )
                )
        );
        client = BlockingNotionClientUtils.asBlockingNotionClient(notionClient);
    }

    private void main() {
        initClient();

        // Get user
        System.out.println("User:");
        User user = client.getUsers().getUser(USER_ID);
        System.out.println(user);

        // Get user list
        System.out.println("User list first page:");
        ResultPage<User> userResultPage = client.getUsers().getUserList(new Pagination());
        System.out.println(userResultPage);

        // Get database
        System.out.println("Database:");
        Database database = client.getDatabases().getDatabase(DATABASE_ID);
        System.out.println(database);
        System.out.println("title=${database.title.plainText}");

        // Query database (simple)
        System.out.println("Simple query results:");
        ResultPage<Page> simpleQueryResultPage = client.getDatabases().queryDatabase(
                DATABASE_ID,
                null,
                null,
                new Pagination()
        );
        System.out.println(simpleQueryResultPage.results);

        // Query database (filters)
        System.out.println("Filtered query results:");
        ResultPage<Page> filteredQueryResultPage = client.getDatabases().queryDatabase(
                DATABASE_ID,
                new DatabaseQuery()
                        .any(
                                new DatabaseQueryPropertyFilter.Text(
                                        "Famous quote",
                                        new DatabaseQueryPredicate.Text.Equals("a")
                                ),
                                new DatabaseQueryPropertyFilter.Text(
                                        "Famous quote",
                                        new DatabaseQueryPredicate.Text.Contains("imp")
                                ),
                                new DatabaseQueryPropertyFilter.Number(
                                        "Legs",
                                        new DatabaseQueryPredicate.Number.GreaterThanOrEqualTo(4)
                                ),
                                new DatabaseQueryPropertyFilter.Formula(
                                        "Legs plus one",
                                        new DatabaseQueryPredicate.Formula.Number.GreaterThan(4)
                                ),
                                new DatabaseQueryPropertyFilter.Checkbox(
                                        "Is Greedo",
                                        new DatabaseQueryPredicate.Checkbox(true)
                                )
                        ),
                new PropertySort()
                        .ascending("Created time")
                        .descending("title"),
                new Pagination()
        );
        System.out.println(filteredQueryResultPage.results);

        // Get page
        System.out.println("Page:");
        Page page = client.getPages().getPage(PAGE_ID);
        System.out.println(page);

        // Create page
        Random random = new Random();
        System.out.println("Created page in database:");
        Page createdPageInDatabase = client.getPages().createPage(
                new DatabaseReference(DATABASE_ID),
                Emoji("⚙️"),
                File("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg️"),
                new PropertyValueList()
                        .number("Legs", random.nextInt())
                        .text("Name", "Name " + random.nextInt())
                        .text("title", "Title " + random.nextInt(), null, new Annotations(Color.BLUE))
                        .text("Oui", new RichTextList()
                                .text("default ", Annotations.DEFAULT)
                                .text("red ", new Annotations(Color.RED))
                                .text("pink background ", new Annotations(Color.PINK_BACKGROUND))
                                .text("bold ", Annotations.BOLD)
                                .text("italic ", Annotations.ITALIC)
                                .text("strikethrough ", Annotations.STRIKETHROUGH)
                                .text("underline ", Annotations.UNDERLINE)
                                .text("code ", Annotations.CODE)
                                .text("mixed", new Annotations(true, true, false, false, false, Color.PURPLE))
                        )
                        .text("Non", new RichTextList()
                                .text("some url", "https://JRAF.org", Annotations.DEFAULT).text("\n", Annotations.DEFAULT)
                                .userMention(USER_ID, Annotations.DEFAULT).text("\n", Annotations.DEFAULT)
                                .databaseMention(DATABASE_ID, Annotations.DEFAULT).text("\n", Annotations.DEFAULT)
                                .pageMention(PAGE_ID, Annotations.DEFAULT).text("\n", Annotations.DEFAULT)
                                .dateMention(new DateTime(new java.util.Date(), "GMT"), null, new Annotations(Color.GREEN)).text("\n", Annotations.DEFAULT)
                                .equation("f(\\relax{x}) = \\int_{-\\infty}^\\infty \\hat f(\\xi)\\,e^{2 \\pi i \\xi x} \\,d\\xi", new Annotations(Color.YELLOW))
                        )
                        .selectByName("Species", "Alien")
                        .multiSelectByNames("Planets", "Tatooine", "Bespin")
                        .date("Some date",
                                new DateOrDateRange(
                                        new DateTime(new java.util.Date(), "GMT"),
                                        new Date(new java.util.Date(System.currentTimeMillis() + 24L * 3600L * 1000L)))
                        )
                        .relation("Android version", PAGE_ID)
                        .people("User", USER_ID)
                        .checkbox("Is Greedo", random.nextBoolean())
                        .email("Email", "aaa@aaa.com")
                        .phoneNumber("Phone", "+1 424 2424 266")
                        .url("Url", "https://zgluteks.com"),
                contentValueList -> {
                    contentValueList.paragraph("test");
                    return Unit.INSTANCE;
                }
        );
        System.out.println(createdPageInDatabase);

        // Update page
        System.out.println("Updated page:");
        Page updatedPage = client.getPages().updatePage(
                PAGE_ID,
                new PropertyValueList()
                        .number("Legs", random.nextInt())
                        .title("Name", "Updated page " + random.nextInt())
                        .text("title", "Updated page " + random.nextInt())
                        .selectByName("Species", "Alien")
                        .multiSelectByNames("Planets", "Tatooine", "Bespin")
                        .date("Some date",
                                new DateOrDateRange(
                                        new DateTime(new java.util.Date(), "GMT"),
                                        new Date(new java.util.Date(System.currentTimeMillis() + 24L * 3600L * 1000L)))
                        )
                        .relation("Android version", PAGE_ID)
                        .people("User", USER_ID)
                        .checkbox("Is Greedo", random.nextBoolean())
                        .email("Email", "aaa@aaa.com")
                        .phoneNumber("Phone", "+1 424 2424 266")
                        .url("Url", "https://zgluteks.com")
        );
        System.out.println(updatedPage);


        // Close
        client.close();
    }

    public static void main(String[] av) {
        new BlockingSample().main();
    }
}

# Changelog

## Next version (????-??-??)

## v1.9.0 (2021-10-23)

- Support more block types following Notion's API evolution - [thanks](https://github.com/BoD/klibnotion/pull/7) [@yujinyan](https://github.com/yujinyan)!
- [Breaking] All properties - including `null` ones - are now always returned on Pages. This means some property types
  that were previously not nullable are now nullable.

## v1.8.0 (2021-10-02)

- Improve support for Files
- Add
  support [Page Icons and Cover Images](https://developers.notion.com/changelog/page-icons-cover-images-new-block-types-and-improved-page-file-properties#:~:text=Page%20Icons%20and%20Cover%20Images)

## v1.7.0 (2021-09-10)

- Add support for [retrieve block API](https://developers.notion.com/reference/retrieve-a-block).
- Add support for [update block API](https://developers.notion.com/reference/update-a-block).
- Add support for [update database API](https://developers.notion.com/reference/update-a-database).
- Add `WorkspaceId` field to `OAuthGetAccessTokenResult`
  following [Notion's API evolution](https://developers.notion.com/changelog/oauth-token-response-now-includes-more-info-about-the-workspace)
- Allow formula properties when creating/updating databases
  following [Notion's API evolution](https://developers.notion.com/changelog/formula-properties-can-now-be-created-in-databases)

## v1.6.0 (2021-07-25)

- Support for create database API
- Add a `Page.setPageArchived(Boolean)` API (thanks [@MaaxGr](https://github.com/MaaxGr))
- `DateTime`s now contain a TimeZone (defaults to the system's), which is correctly sent to / parsed from the API (
  resolves [issue #4](https://github.com/BoD/klibnotion/issues/4))
- Add `url` field to Page
  following [Notion's API evolution](https://developers.notion.com/changelog/page-objects-now-return-url)
- Add `parent` field to Database
  following [Notion's API evolution](https://developers.notion.com/changelog/database-objects-now-return-parent)
- Add missing `created` and `lastEdited` fields to Block

## v1.5.2 (2021-06-01)

- Fix title property type (title instead of rich text) when creating a page with a page parent.

## v1.5.1 (2021-05-30)

- Make `OAuthGetAccessTokenResult.workspaceIcon` nullable
  (resolves [issue #1](https://github.com/BoD/klibnotion/issues/1))
- Don't send `null` for empty children when creating a page
(resolves [issue #2](https://github.com/BoD/klibnotion/issues/2))

## v1.5.0 (2021-05-16)

- Add a `getAllBlockListRecursively` API to easily retrieve a whole document.
- (Breaking change) use of Sealed Interfaces - a few interfaces were moved to their parent package due to this change
- Use the new syntax for writing property values
- Add ability to create a page inside a page (but currently doesn't work on the Notion API side)
- Add `Notion-Version` header to all calls
- Support for search APIs
- Support for OAuth
- Improve error handling
- Support for anonymous users

## v1.4.0 (2021-04-18)

- Remove `isArchived` parameter from `getPage` following the Notion API change.
- Fix write content API following the Notion API change.
- Support for "Retrieve block children" API
- Support for "Append block children" API

## v1.3.0 (2021-04-03)

- Support for "get database list" API

## v1.2.0 (2021-03-21)

- Support for content in page create API

## v1.1.0 (2021-02-27)

- Support for Array Rollup property type
- Slightly improve `DatabaseQuery` API (`any` instead of `addAnyFilters`, `all` instead of `addAllFilters`)
- Slightly improve `DatabaseQuerySort` API (`ascending` and `descending` instead of `add`)
- Add a `bypassSslChecks` option on `HttpConfiguration` (should never be used in production)

## v1.0.0 (2021-02-14)

Initial release.

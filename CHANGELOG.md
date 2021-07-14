# Changelog

## v1.6.0 (2021-07-14)

- Add a `Page.setPageArchived(Boolean)` API (thanks [@MaaxGr](https://github.com/MaaxGr))

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

# Changelog

All notable changes to this project will be documented in this file.

The format is inspired by Keep a Changelog and this project follows Semantic Versioning.

## [0.0.9] - 2026-04-13

### Added
- Added `getFullUserInfoPlain(String sub)` to retrieve the full Keycloak user representation as raw JSON.
- Added overloaded `updateUser(String sub, String user)` to update a user using full JSON payload.

### Changed
- Updated user attributes flow to operate on the full user JSON document:
- `addUserAttributes(...)` now parses the full user JSON, merges the requested attributes into the `attributes` object, and sends back the full updated payload.
- `deleteUserAttributes(...)` now parses the full user JSON, removes the selected keys from the `attributes` object, and sends back the full updated payload.
- Updated internal token authorization flow for safer concurrent access.

### Fixed
- Fixed user attributes update behavior that previously relied on manually concatenated JSON fragments.
- Fixed user attributes deletion/update reliability by serializing and submitting a valid full JSON payload.
- Fixed concurrent request risk in singleton service by removing shared mutable request state (`HttpHeaders` and auth form map) and creating request-local instances.
- Fixed visibility/race-risk on token cache fields by aligning synchronization and field visibility semantics.

### Removed
- Removed public `callForAddAttributes(String sub, String payload)` from the API surface.

### Dependencies
- Updated `spring-boot-starter` to `3.5.13`.
- Updated `spring-boot-starter-web` to `3.5.13`.
- Updated `spring-boot-autoconfigure-processor` to `3.5.13`.
- Updated `gson` to `2.13.2`.

### Documentation
- Updated README method table to match current public API.
- Updated README examples for `SearchUserType`, role methods signatures, full JSON methods, and removed outdated `callForAddAttributes` usage.

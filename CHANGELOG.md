# Changelog

## [2.0.0] - 2026-02-23

### Changed
- Required Gradle version is now 9.1.0
- Class Launcher converted to Java record

### Removed
- Deprecated option `bindServices`

## [1.7.6] - 2025-09-23

### Fixed
- `linuxPackageDeps` wrong type

## [1.7.5] - 2025-08-29

### Fixed
- `getTemp()` converted to string property to fix configuration issues

## [1.7.4] - 2025-08-25

### Fixed
- `getTemp()` should be input directory

## [1.7.3] - 2025-06-17

### Fixed:
- wrong `getMainJar()` annotation

## [1.7.2] - 2025-06-16

### Fixed
- `mainJar` not set properly

## [1.7.1] - 2025-06-13

### Changed
- Properties types changed:
    - `appContent`
    - `destination`
    - `fileAssociations`
    - `icon`
    - `input`
    - `launchers`
    - `licenseFile`
    - `macDmgContent`
    - `macEntitlements`
    - `modulePaths`
    - `resourceDir`
    - `runtimeImage`
    - `temp`
- Destination directory is now always removed
- removeDestination property removed
- up-to-date default removed

## [1.7.0] - 2025-06-07

### Changed
- Java Bean properties replaced with Gradle properties - build scripts may require changes
- Configuration cache compatibility
- Version check for parameters removed. Users are advised to consult `jpackage` manual

## [1.6.1] - 2025-02-01

### Added
- Option: `removeDestination`

## [1.6.0] - 2023-11-23

### Changed
- Gradle version increased to 7.4
- `jpackage` task is declared not compatible with configuration cache

## [1.5.2] - 2023-04-04

### Changed
- Version detection reimplemented via `-XshowSettings:properties`

## [1.5.1] - 2023-01-20

### Fixed
- Task does not show up in ./gradlew tasks

## [1.5.0] - 2022-11-04

### Added
- Introduced `jpackage` version check for parameters
- Added parameters:
    - --about-url
    - --app-content
    - --bind-services
    - --jlink-options
    - --launcher-as-service
    - --linux-package-deps
    - --mac-app-category
    - --mac-app-store
    - --mac-dmg-content
    - --mac-entitlements
    - --win-help-url
    - --win-shortcut-prompt
    - --win-update-url

## [1.4.1] - 2022-09-19

### Fixed
- Potential NPE

## [1.4.0] - 2022-09-19

### Changed
- Bump gradle version to 6.7.1
- `jpackage` environment variables

## [1.3.1] - 2021-04-23

### Changed
- Workaround for Groovy type ignorance
- `jpackage` task parameters marked as optional

## [1.3.0] - 2021-04-05

### Added
- Support for additional parameters
- Launcher file resolution unified with other file parameters
- Dry run mode

## [1.2.0] - 2021-03-28

### Removed
- Single `modulePath` removed

### Changed
- File and directory based parameters resolution

## [1.1.1] - 2021-03-02

### Added
- `appImage`
- `winConsole`

## [1.1.0] - 2021-02-27

### Added
- `fileAssociations`
- `launchers`
- `addModules`
- multiple `modulePath` parameters

### Deprecated
- single `modulePath` parameter

## [1.0.2] - 2021-01-17

## [1.0.1] - 2021-01-17

## [1.0.0] - 2021-01-17

## [0.0.3] - 2021-01-16

### Added
- Toolchain support, requires Gradle 6.7+

## [0.0.2] - 2020-07-06

### Added
- OS specific configuration DSL

## [0.0.1] - 2020-07-02

### Added
- Initial version, ported from JPackage Maven Plugin
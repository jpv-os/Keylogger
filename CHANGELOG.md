<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Keylogger Changelog

## [Unreleased]

## [0.7.1] - 2024-06-17

### Changed

- Improved the settings labels for the new insert and update intervals

## [0.7.0] - 2024-06-17

### Changed

- Configurable intervals for inserting data into the action database and updating the toolwindow content
- Visit the plugin settings to find more information about the new settings

## [0.6.0] - 2024-06-17

- Changelog update - `v0.5.4` by @github-actions in https://github.com/jpv-os/Keylogger/pull/39
- Bump JetBrains/qodana-action from 2024.1.4 to 2024.1.5 by @dependabot in https://github.com/jpv-os/Keylogger/pull/40
- Bump org.xerial:sqlite-jdbc from 3.45.3.0 to 3.46.0.0 by @dependabot in https://github.com/jpv-os/Keylogger/pull/43
- Bump org.jetbrains.kotlin.jvm from 1.9.24 to 2.0.0 by @dependabot in https://github.com/jpv-os/Keylogger/pull/42
- Bump org.jetbrains.kotlinx.kover from 0.7.6 to 0.8.1 by @dependabot in https://github.com/jpv-os/Keylogger/pull/44
- Bump gradle/wrapper-validation-action from 3.3.2 to 3.4.0 by @dependabot in https://github.com/jpv-os/Keylogger/pull/45
- Bump org.jetbrains.intellij from 1.17.3 to 1.17.4 by @dependabot in https://github.com/jpv-os/Keylogger/pull/47

## [0.5.4] - 2024-05-13

- Bump gradle/wrapper-validation-action from 3.3.1 to 3.3.2 by @dependabot in https://github.com/jpv-os/Keylogger/pull/33
- Bump JetBrains/qodana-action from 2023.3.1 to 2024.1.2 by @dependabot in https://github.com/jpv-os/Keylogger/pull/34
- Changelog update - `v0.5.1` by @github-actions in https://github.com/jpv-os/Keylogger/pull/32
- Bump JetBrains/qodana-action from 2024.1.2 to 2024.1.3 by @dependabot in https://github.com/jpv-os/Keylogger/pull/35
- Bump org.jetbrains.kotlin.jvm from 1.9.23 to 1.9.24 by @dependabot in https://github.com/jpv-os/Keylogger/pull/36
- Bump JetBrains/qodana-action from 2024.1.3 to 2024.1.4 by @dependabot in https://github.com/jpv-os/Keylogger/pull/37
- Bump org.knowm.xchart:xchart from 3.8.7 to 3.8.8 by @dependabot in https://github.com/jpv-os/Keylogger/pull/38

## [0.5.1] - 2024-04-26

- Bump org.jetbrains.intellij from 1.17.2 to 1.17.3 by @dependabot in https://github.com/jpv-os/Keylogger/pull/26
- Bump org.xerial:sqlite-jdbc from 3.45.1.0 to 3.45.3.0 by @dependabot in https://github.com/jpv-os/Keylogger/pull/29
- Bump gradle/wrapper-validation-action from 2.1.1 to 3.3.1 by @dependabot in https://github.com/jpv-os/Keylogger/pull/30

## [0.5.0] - 2024-03-07

### Added

- Tool window tab with charts

## [0.4.0] - 2024-03-03

### Added

- Custom cell renderers for tool window tables
- Settings page screenshot

### Changed

- Updated README with more details and roadmap
- Code cleanup, added documentation

## [0.3.1] - 2024-02-29

### Changed

- README updated

## [0.3.0] - 2024-02-28

### Changed

- Breaking change: settings now uses a different persistence key, so the previous settings will be lost.
- Greatly improved the plugin settings page
- Added a new setting for using database file paths relative to the default home directory for better interop with
  Settings Sync
- Optimize plugin description length for the JetBrains Marketplace layout

## [0.2.1] - 2024-02-24

### Added

- Repository and build process configuration

## [0.2.0] - 2024-02-22

### Added

- Action history tab in tool window
- IdeaVim compatibility mode: in INSERT mode, backspace emits two events, the regular backspace event and the custom
  IdeaVim event "Shortcuts", which is ignored if the plugin is in IdeaVim compatibility mode.
- Custom plugin and toolwindow icons

### Changed

- Plugin description section in the README file

### Removed

- Removed the `pluginUntilBuild` limitation that prevented the plugin from being installed on early access builds

## [0.1.1] - 2024-02-20

### Changed

- Updated README with more details and roadmap

## [0.1.0] - 2024-02-16

### Added

- Initial public release

## [0.0.1] - 2024-02-09

### Added

- Prototype implementation of action/click/type listeners and basic services
- Simple tool window to display actions per minute
- Setup README and CHANGELOG
- Generate JetBrains marketplace plugin ID with initial hidden release
- Store data in SQLite database
- Make database URL and idle timeout configurable via plugin settings
- Add a simple settings page to configure database URL and idle timeout

[Unreleased]: https://github.com/jpv-os/Keylogger/compare/v0.7.1...HEAD
[0.7.1]: https://github.com/jpv-os/Keylogger/compare/v0.7.0...v0.7.1
[0.7.0]: https://github.com/jpv-os/Keylogger/compare/v0.6.0...v0.7.0
[0.6.0]: https://github.com/jpv-os/Keylogger/compare/v0.5.4...v0.6.0
[0.5.4]: https://github.com/jpv-os/Keylogger/compare/v0.5.1...v0.5.4
[0.5.1]: https://github.com/jpv-os/Keylogger/compare/v0.5.0...v0.5.1
[0.5.0]: https://github.com/jpv-os/Keylogger/compare/v0.4.0...v0.5.0
[0.4.0]: https://github.com/jpv-os/Keylogger/compare/v0.3.1...v0.4.0
[0.3.1]: https://github.com/jpv-os/Keylogger/compare/v0.3.0...v0.3.1
[0.3.0]: https://github.com/jpv-os/Keylogger/compare/v0.2.1...v0.3.0
[0.2.1]: https://github.com/jpv-os/Keylogger/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/jpv-os/Keylogger/compare/v0.1.1...v0.2.0
[0.1.1]: https://github.com/jpv-os/Keylogger/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/jpv-os/Keylogger/compare/v0.0.1...v0.1.0
[0.0.1]: https://github.com/jpv-os/Keylogger/commits/v0.0.1

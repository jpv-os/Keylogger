<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Keylogger Changelog

## [Unreleased]

### Changed

- Code cleanup, added documentation

## [0.3.1] - 2024-02-29

### Changed

- README updated

## [0.3.0] - 2024-02-28

### Changed

- Breaking change: settings now uses a different persistence key, so the previous settings will be lost.
- Greatly improved the plugin settings page
- Added a new setting for using database file paths relative to the default home directory for better interop with Settings Sync
- Optimize plugin description length for the JetBrains Marketplace layout

## [0.2.1] - 2024-02-24

### Added

- Repository and build process configuration

## [0.2.0] - 2024-02-22

### Added

- Action history tab in tool window
- IdeaVim compatibility mode: in INSERT mode, backspace emits two events, the regular backspace event and the custom IdeaVim event "Shortcuts", which is ignored if the plugin is in IdeaVim compatibility mode.
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

[Unreleased]: https://github.com/jpv-os/Keylogger/compare/v0.3.1...HEAD
[0.3.1]: https://github.com/jpv-os/Keylogger/compare/v0.3.0...v0.3.1
[0.3.0]: https://github.com/jpv-os/Keylogger/compare/v0.2.1...v0.3.0
[0.2.1]: https://github.com/jpv-os/Keylogger/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/jpv-os/Keylogger/compare/v0.1.1...v0.2.0
[0.1.1]: https://github.com/jpv-os/Keylogger/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/jpv-os/Keylogger/compare/v0.0.1...v0.1.0
[0.0.1]: https://github.com/jpv-os/Keylogger/commits/v0.0.1

<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Keylogger Changelog

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

## [Unreleased]

### Added

- Initial scaffold created
  from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
- Prototype implementation of action/click/type listeners and basic services
- Simple tool window to display actions per minute
- Setup README and CHANGELOG
- Generate JetBrains marketplace plugin ID with initial hidden release
- Store data in SQLite database
- Make database URL and idle timeout configurable via plugin settings
- Add a simple settings page to configure database URL and idle timeout

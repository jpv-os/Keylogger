# Keylogger

![Build](https://github.com/jpv-os/Keylogger/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/23746-keylogger.svg)](https://plugins.jetbrains.com/plugin/23746-keylogger)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/23746-keylogger.svg)](https://plugins.jetbrains.com/plugin/23746-keylogger)

<!-- Plugin description -->

**Keylogger** is an open source plugin that tracks your actions in the editor, keeps a history and shows statistics.

<!-- Plugin description end -->

---

## Screenshots

<div style="display: flex;">

![Screenshot 1](https://raw.githubusercontent.com/jpv-os/Keylogger/main/assets/screenshot1.png)

![Screenshot 2](https://raw.githubusercontent.com/jpv-os/Keylogger/main/assets/screenshot2.png)

![Screenshot 3](https://raw.githubusercontent.com/jpv-os/Keylogger/main/assets/screenshot3.png)

</div>

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Keylogger"</kbd> >
  <kbd>Install</kbd>

- Manually:

  Download the [latest release](https://github.com/jpv-os/Keylogger/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Getting Started

- **Customize Keylogger** in the IDE Settings under <kbd>Settings/Preferences</kbd> > <kbd>Tools</kbd> > <kbd>Keylogger</kbd>
- **Look at your statistics** in the Keylogger Tool Window under <kbd>View</kbd> > <kbd>Tool Windows</kbd> > <kbd>Keylogger</kbd>
- **Access the raw data** on your machine using <kbd>sqlite3</kbd>
- Visit the [plugin page on GitHub](https://github.com/jpv-os/Keylogger) for more information, to report bugs and to suggest features
- Consider leaving a review on the [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/23746-keylogger) if you enjoy Keylogger

## Development Roadmap

This plugin is still in early development. All versions below 1.0.0 are considered unstable. 

- [ ] Version 1.0.0
    - [ ] Improve error handling, make critical features more robust (what happens if database file is deleted while
    running, ...)
    - [ ] Logging for critical features (DB connection) to make error reporting easier for users
    - [ ] Comprehensive usage guide (also as YouTube video?)
    - [ ] Code fully documented
    - [ ] Demo video (must be hosted on YouTube)
- [ ] Feature ideas
  - [ ] Write meaningful tests (Unit tests, UI tests, ...?)
  - [ ] Welcome Dialog / First Run Wizard
  - [ ] Notifications (e.g. "Database successfully deleted", "1 million actions reached" etc.)
  - [ ] Configurable Action ignore list (currently, the IdeaVim compatibility mode ignoes the "Shortcuts" action - why
    not let the user create an ignore list?)
  - [ ] Database storage
    - [ ] Store actions database in plugin settings instead of file, so that it works with settings sync (allowed maximum size?)
    - [ ] Encrypted database file (configurable via settings)
  - [ ] IDE status bar widget that shows the current APM (or other statistics)
  - [ ] IDE progress bar that shows the idle timeout
  - [ ] Persistent state component for customizable toolwindow layout
  - [ ] Scroll Listeners, and "How many meters scrolled" statistic

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation

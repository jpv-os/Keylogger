# Keylogger

![Build](https://github.com/jpv-os/Keylogger/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/23746-keylogger.svg)](https://plugins.jetbrains.com/plugin/23746-keylogger)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/23746-keylogger.svg)](https://plugins.jetbrains.com/plugin/23746-keylogger)

<!-- Plugin description -->

## Features

- Listens for actions in the editor (typing, clicking, shortcuts)
- Tracks active and idle time
- Stores the actions in a local SQLite database file
- Shows a simple tool window with an action counter and statistics like actions per minute
- Allows customizing the database file location and idle timeout

<!-- Plugin description end -->

---

## Screenshots

<div style="display: flex;">

![Screenshot 1](https://raw.githubusercontent.com/jpv-os/Keylogger/main/assets/screenshot1.png)

![Screenshot 2](https://raw.githubusercontent.com/jpv-os/Keylogger/main/assets/screenshot2.png)

</div>


## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Keylogger"</kbd> >
  <kbd>Install</kbd>

- Manually:

  Download the [latest release](https://github.com/jpv-os/Keylogger/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


## Development Roadmap

This plugin is still in its early stages. The following roadmap is a rough outline of the next steps

- [ ] Start with template project
    - [x] Create a new [IntelliJ Platform Plugin Template][template] project.
    - [x] Get familiar with the [template documentation][template].
    - [x] Adjust the [pluginGroup](./gradle.properties), [plugin ID](./src/main/resources/META-INF/plugin.xml)
      and [sources package](./src/main/kotlin).
    - [x] Adjust the plugin description in `README` (see [Tips][docs:plugin-description])
    - [x] Review
      the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html?from=IJPluginTemplate).
    - [x] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate)
      for the first time.
    - [x] Set the `PLUGIN_ID` in the above README badges.
    - [ ] Set
      the [Plugin Signing](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html?from=IJPluginTemplate)
      related [secrets](https://github.com/JetBrains/intellij-platform-plugin-template#environment-variables).
    - [ ] Set
      the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html?from=IJPluginTemplate).
    - [x] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be
      notified about releases containing new features and fixes.
- [x] Prototype
    - [x] Action/click/type listeners
    - [x] Create a simple tool window
    - [x] First (hidden) release
- [x] Minimum viable plugin
    - [x] Persist data
    - [x] Make things like idle timeout configurable via plugin settings
- [ ] General improvements 
    - [x] Restore default settings button, delete database button in settings page
    - [ ] Listen to "Editor Active" events to make sure the idle timeout is accurate and only one session is recorded
    - [ ] Improve UI by implementing custom cell renderers etc. for tables
    - [ ] Database schema validation on startup
- [ ] Code Quality
    - [ ] Logging
    - [ ] Write meaningful tests
    - [ ] Improve error handling
    - [ ] Dependabot version bumps
    - [ ] Host code reports on GitHub Pages
- [ ] Future feature ideas
    - [ ] Welcome Dialog
    - [ ] Notifications (e.g. "Database successfully deleted")
    - [ ] History of actions
    - [ ] Store actions database in plugin settings, so that it works with settings sync (allowed maximum size?)
    - [ ] Charts and visualizations in tool window
    - [ ] IDE status bar widget that shows the current APM (or other statistics)
    - [ ] IDE progress bar that shows the idle timeout
    - [ ] Persistent state component for customizable toolwindow layout
    - [ ] Write a devlog and publish it on GitHub Pages
- [ ] Version 1.0.0
    - [ ] Custom SVG icon
    - [ ] CI/CD pipeline complete
    - [ ] Demo video (must be hosted on YouTube)
    - [ ] Comprehensive usage guide (also as YouTube video?)

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation

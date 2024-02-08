# Keylogger

![Build](https://github.com/jpv-os/Keylogger/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->
This plugin provides a simple tool window to see your actions per minute when typing, clicking or performing IDE actions in the editor.
<!-- Plugin description end -->

## Roadmap
- [ ] Start with template project
  - [x] Create a new [IntelliJ Platform Plugin Template][template] project.
  - [x] Get familiar with the [template documentation][template].
  - [x] Adjust the [pluginGroup](./gradle.properties), [plugin ID](./src/main/resources/META-INF/plugin.xml) and [sources package](./src/main/kotlin).
  - [x] Adjust the plugin description in `README` (see [Tips][docs:plugin-description])
  - [x] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html?from=IJPluginTemplate).
  - [x] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
  - [ ] Set the `PLUGIN_ID` in the above README badges.
  - [ ] Set the [Plugin Signing](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html?from=IJPluginTemplate) related [secrets](https://github.com/JetBrains/intellij-platform-plugin-template#environment-variables).
  - [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html?from=IJPluginTemplate).
  - [x] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.
- [x] Prototype
  - [x] Action/click/type listeners
  - [x] Create a simple tool window
  - [x] First (hidden) release
- [ ] Minimum viable plugin
  - [ ] Persist data
  - [ ] Make things like idle timeout configurable via plugin settings
- [ ] General improvements before continuing
  - [ ] Improve UI by implementing custom cell renderers etc. for tables
    - [ ] Follow Jetbrains UX recommendations for displaying text
  - [ ] Remove all TODOs from code and collect here if necessary
- [ ] Future feature ideas
  - [ ] Charts and visualizations
  - [ ] Status bar widget
  - [ ] Data export

## Development

For general development information, visit the [IntelliJ Platform Plugin Template][template] repository.

Visit the plugin repository at [github.com/jpv-os/Keylogger](https://github.com/jpv-os/Keylogger)
for the latest information and to report issues. Pull requests are welcome.

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Keylogger"</kbd> >
  <kbd>Install</kbd>

- Manually:

  Download the [latest release](https://github.com/jpv-os/Keylogger/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation

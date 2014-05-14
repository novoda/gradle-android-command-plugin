# 1.2.1

- add descriptions to tasks and group them together (instead of "Other Tasks")

# 1.2.0

Complete rewrite of the plugin.

### New commands

Replaced generic ADB command with useful predefined tasks:
- `installDevice<Variant>`
- `uninstallDevice<Variant>`
- `run<Variant>`
- `monkey<Variant>` - the default number of monkeyrunner events is 10000, change via setting _android.events_
- `clearPreferences<Variant>`

See [README](https://github.com/novoda/gradle-android-command-plugin/blob/master/README.md) for examples.

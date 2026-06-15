# Spotube Gradle Plugin

A Gradle plugin for Spotube that packages Zipline executables into `.smplug` files for Spotube plugin projects.

## Overview

This plugin automatically creates packaging tasks when applied alongside the [app.cash.zipline](https://cashapp.github.io/zipline/) plugin. It bundles Zipline compiler outputs with your `plugin.json` into distributable `.smplug` files.

## Requirements

- Gradle 9.4+
- Kotlin JVM plugin
- Zipline

## Installation

### Using the Plugin DSL (Groovy)

```groovy
plugins {
    id 'dev.krtirtho.spotube.gradle-plugin' version '0.1.0'
}
```

### Using the Plugin DSL (Kotlin)

```kotlin
plugins {
    id("dev.krtirtho.spotube.gradle-plugin") version "0.1.0"
}
```

### Using Version Catalogs

Add the plugin to your version catalog (`gradle/libs.versions.toml`):

```toml
[plugins]
spotube = { id = "dev.krtirtho.spotube.gradle-plugin", version = "0.1.0" }
```

Then reference it in your build files:

```kotlin
// build.gradle.kts
plugins {
    alias(libs.plugins.spotube)
}
```

## Usage

Apply the plugin after the Zipline plugin:

```kotlin
plugins {
    id("app.cash.zipline")
    id("dev.krtirtho.spotube.gradle-plugin")
}
```

The plugin automatically registers two tasks:

| Task | Description |
|------|-------------|
| `packageDevelopmentPlugin` | Packages the development Zipline build into `plugin-development.smplug` |
| `packageProductionPlugin` | Packages the production Zipline build into `plugin-production.smplug` |

Output files are placed in `build/distributions/`.

### Prerequisites

Ensure your project contains a `plugin.json` file at the project root, as it is automatically included in the package.

## Tasks

```
$ ./gradlew tasks --group distribution

Distribution tasks
------------------
packageDevelopmentPlugin - Packages the development Zipline executable and plugin.json into a smplug.
packageProductionPlugin  - Packages the production Zipline executable and plugin.json into a smplug.
```

## License

Copyright (C) 2026 spotube.cc

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this software except in compliance with the License.

You may obtain a copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

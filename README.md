# JPackage Gradle Plugin

Gradle plugin for [jpackage](https://openjdk.java.net/jeps/343) tool.

[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/org/panteleyev/jpackageplugin/org.panteleyev.jpackageplugin.gradle.plugin/maven-metadata.xml.svg?label=Gradle%20Plugin)](https://plugins.gradle.org/plugin/org.panteleyev.jpackageplugin)
[![Gradle](https://img.shields.io/badge/Gradle-7.4%2B-green)](https://gradle.org/)
[![Java](https://img.shields.io/badge/Java-8-orange?logo=java)](https://www.oracle.com/java/technologies/javase-downloads.html)
[![GitHub](https://img.shields.io/github/license/petr-panteleyev/jpackage-gradle-plugin)](LICENSE)

## Finding jpackage

Plugin searches for ```jpackage``` executable using the following priority list:

1. Configured toolchain

2. ```java.home``` system property.

Though rarely required it is possible to override toolchain for particular ```jpackage``` task:

```kotlin
javaLauncher = javaToolchains.launcherFor {
    languageVersion = JavaLanguageVersion.of(21)
}
```

## Configuration

There are generic ```jpackage``` parameters as well as OS-specific parameters for OS X, Linux, Windows.
OS-specific parameters are processed when build is done on the corresponding OS.

If some generic parameters should have different values based on OS then they should be placed into configuration
blocks:

* windows
* mac
* linux

*Example:*

```kotlin
// Windows specific parameters will be processed only during Windows build
winMenu = true
winDirChooser = true

mac {
    // Generic parameter value for OS X build
    icon = layout.projectDirectory.file("icons/icons.icns")
}

windows {
    // Generic parameter value for Windows build
    icon = layout.projectDirectory.file("icons/icons.ico")
}
```

### Parameters

| Parameter               | Type                       | JPackage Argument                                            | Min Version | Max Version |
|-------------------------|----------------------------|--------------------------------------------------------------|-------------|-------------|
| **Generic**             |
| aboutUrl                | Property&lt;String>        | --about-url &lt;url>                                         | 17          | *           |
| addModules              | ListProperty&lt;String>    | --add-modules &lt;module>[,&lt;module>]                      | 14          | *           |
| appDescription          | Property&lt;String>        | --description &lt;description string>                        | 14          | *           |
| appContent              | ConfigurableFileCollection | --app-content additional-content[,additional-content...]     | 18          | *           |
| appImage                | Property&lt;String>        | --app-image &lt;name>                                        | 14          | *           |
| appName                 | Property&lt;String>        | --name &lt;name>                                             | 14          | *           |
| appVersion              | Property&lt;String>        | --app-version &lt;version>                                   | 14          | *           |
| arguments               | ListProperty&lt;String>    | --arguments &lt;main class arguments>                        | 14          | *           |
| bindServices            | Property&lt;Boolean>       | --bind-services                                              | 14          | 15          |
| copyright               | Property&lt;String>        | --copyright &lt;copyright string>                            | 14          | *           |
| destination             | DirectoryProperty          | --dest &lt;destination path>                                 | 14          | *           |
| fileAssociations        | ConfigurableFileCollection | --file-associations &lt;file association property file>      | 14          | *           |
| icon                    | RegularFileProperty        | --icon &lt;icon file path>                                   | 14          | *           |
| input                   | DirectoryProperty          | --input &lt;input path>                                      | 14          | *           |
| installDir              | Property&lt;String>        | --install-dir &lt;file path>                                 | 14          | *           |
| javaOptions             | ListProperty&lt;String>    | --java-options &lt;options>                                  | 14          | *           |
| jLinkOptions            | ListProperty&lt;String>    | --jlink-options &lt;options>                                 | 16          | *           |
| launchers               | ListProperty&lt;Launcher>  | --add-launcher &lt;name>=&lt;property file>                  | 14          | *           |
| launcherAsService       | Property&lt;Boolean>       | --launcher-as-service                                        | 19          | *           |
| licenseFile             | RegularFileProperty        | --license-file &lt;license file path>                        | 14          | *           |
| mainClass               | Property&lt;String>        | --main-class &lt;class name>                                 | 14          | *           |
| mainJar                 | RegularFileProperty        | --main-jar &lt;main jar file>                                | 14          | *           |
| module                  | Property&lt;String>        | --module &lt;module name>[/&lt;main class>]                  | 14          | *           |
| modulePaths             | ConfigurableFileCollection | --module-path &lt;module path>                               | 14          | *           |
| resourceDir             | DirectoryProperty          | --resource-dir &lt;resource dir path>                        | 14          | *           |
| runtimeImage            | DirectoryProperty          | --runtime-image &lt;file path>                               | 14          | *           |
| temp                    | DirectoryProperty          | --temp &lt;temp dir path>                                    | 14          | *           |
| type                    | Property&lt;ImageType>     | --type &lt;type>                                             | 14          | *           |
| vendor                  | Property&lt;String>        | --vendor &lt;vendor string>                                  | 14          | *           |
| verbose                 | Property&lt;Boolean>       | --verbose                                                    | 14          | *           |
| **Windows**             |
| winConsole              | Property&lt;Boolean>       | --win-console                                                | 14          | *           |
| winDirChooser           | Property&lt;Boolean>       | --win-dir-chooser                                            | 14          | *           |
| winHelpUrl              | Property&lt;String>        | --win-help-url &lt;url>                                      | 17          | *           |
| winMenu                 | Property&lt;Boolean>       | --win-menu                                                   | 14          | *           |
| winMenuGroup            | Property&lt;String>        | --win-menu-group &lt;menu group name>                        | 14          | *           |
| winPerUserInstall       | Property&lt;Boolean>       | --win-per-user-install                                       | 14          | *           |
| winShortcut             | Property&lt;Boolean>       | --win-shortcut                                               | 14          | *           |
| winShortcutPrompt       | Property&lt;Boolean>       | --win-shortcut-prompt                                        | 17          | *           |
| winUpdateUrl            | Property&lt;String>        | --win-update-url &lt;url>                                    | 17          | *           |
| winUpgradeUuid          | Property&lt;String>        | --win-upgrade-uuid &lt;id string>                            | 14          | *           |
| **OS X**                |
| macAppCategory          | Property&lt;String>        | --mac-app-category &lt;category string>                      | 17          | *           |
| macAppStore             | Property&lt;Boolean>       | --mac-app-store                                              | 17          | *           |
| macBundleSigningPrefix  | Property&lt;String>        | --mac-bundle-signing-prefix &lt;prefix string>               | 14          | 16          |
| macDmgContent           | ConfigurableFileCollection | --mac-dmg-content additional-content[,additional-content...] | 18          | *           |
| macEntitlements         | RegularFileProperty        | --mac-entitlements &lt;file path>                            | 17          | *           |
| macPackageIdentifier    | Property&lt;String>        | --mac-package-identifier &lt;ID string>                      | 14          | *           |
| macPackageName          | Property&lt;String>        | --mac-package-name &lt;name string>                          | 14          | *           |
| macPackageSigningPrefix | Property&lt;String>        | --mac-package-signing-prefix &lt;prefix string>              | 17          | *           |
| macSign                 | Property&lt;Boolean>       | --mac-sign                                                   | 14          | *           |
| macSigningKeychain      | Property&lt;String>        | --mac-signing-keychain &lt;keychain name>                    | 14          | *           |
| macSigningKeyUserName   | Property&lt;String>        | --mac-signing-key-user-name &lt;team name>                   | 14          | *           |
| **Linux**               |
| linuxAppCategory        | Property&lt;String>        | --linux-app-category &lt;category value>                     | 14          | *           |
| linuxAppRelease         | Property&lt;String>        | --linux-app-release &lt;release value>                       | 14          | *           |
| linuxDebMaintainer      | Property&lt;String>        | --linux-deb-maintainer &lt;email address>                    | 14          | *           |
| linuxMenuGroup          | Property&lt;String>        | --linux-menu-group &lt;menu-group-name>                      | 14          | *           |
| linuxPackageName        | Property&lt;String>        | --linux-package-name &lt;package name>                       | 14          | *           |
| linuxPackageDeps        | Property&lt;Boolean>       | --linux-package-deps                                         | 14          | *           |
| linuxRpmLicenseType     | Property&lt;String>        | --linux-rpm-license-type &lt;type string>                    | 14          | *           |
| linuxShortcut           | Property&lt;Boolean>       | --linux-shortcut                                             | 14          | *           |

Since version ```1.7.0``` the plugin does not check if parameter is applicable to ```jpackage``` tool version.
Users are advised to consult the corresponding User's Guide.

### Image Type

| Plugin Value | JPackage Type                   |
|--------------|---------------------------------|
| DEFAULT      | Default image type, OS specific |
| APP_IMAGE    | app-image                       |
| DMG          | dmg                             |
| PKG          | pkg                             |
| EXE          | exe                             |
| MSI          | msi                             |
| RPM          | rpm                             |
| DEB          | deb                             |

### Launchers

Launchers are defines using class ```Launcher```:

```kotlin
launchers = listOf(
    Launcher("launcher_1", layout.projectDirectory.file("launcher_1.properties").asFile),
    Launcher("launcher_2", layout.projectDirectory.file("launcher_2.properties").asFile)
)
```

### Destination Directory

```jpackage``` utility fails if output directory already exists. At the same time gradle always creates plugin output
directory.

In order to work around this behaviour plugin always tries to delete directory specified by ```destination``` before
launching ```jpackage```.

For safety reasons ```destination``` must point to the location inside ```${layout.buildDirectory}```.

_Example:_

```kotlin
destination = layout.buildDirectory.dir("dist")
```

### Default Command-Line Arguments

Default command line arguments are passed to the main class when the application is started without providing arguments.

_Example:_

```kotlin
arguments = listOf(
    "SomeArgument",
    "Argument with spaces",
    "Argument with \"quotes\""
)
```

### JVM Options

Options that are passed to the JVM when the application is started.

_Example:_

```kotlin
javaOptions = listOf(
    "-Xms2m",
    "-Xmx10m"
)
```

### jlink options

Options that are passed to underlying jlink call.

_Example:_

```kotlin
jLinkOptions = listOf(
    "--strip-native-commands",
    "--strip-debug"
)
```

### jpackage Environment Variables

Optionally environment variables can be passed to ```jpackage``` executable process.

_Example:_

```kotlin
jpackageEnvironment = mapOf(
    "GRADLE_DIR" to project.projectDir.absolutePath,
    "BUILD_DIR" to project.buildDir.absolutePath
)
```

```null``` values as well as ```null``` or empty keys are ignored.

## Logging

Plugin uses ```LogLevel.INFO``` to print various information about toolchain, jpackage parameters, etc. Use gradle
option ```--info``` to check this output.

## Dry Run Mode

To execute plugin tasks in dry run mode without calling ```jpackage``` set property```jpackage.dryRun``` to true.

_Example:_

```shell
$ ./gradlew clean build jpackage --info -Djpackage.dryRun=true
```

## Configuration Cache

This plugin should be compatible with
Gradle [configuration cache](https://docs.gradle.org/current/userguide/configuration_cache.html).

## Examples

* [Modular Application with Full Runtime](doc/examples/ModularFullRuntime.md)
* [Modular Application with jlink](doc/examples/ModularFromJlink.md)
* [Non-Modular Application](doc/examples/Non-ModularApplication.md)

## References

[Packaging Tool User's Guide](https://docs.oracle.com/en/java/javase/24/jpackage/packaging-tool-user-guide.pdf)

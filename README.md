# JPackage Gradle Plugin

Gradle plugin for [jpackage](https://openjdk.java.net/jeps/343) tool available since JDK-14.

[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/org/panteleyev/jpackageplugin/org.panteleyev.jpackageplugin.gradle.plugin/maven-metadata.xml.svg?label=Gradle%20Plugin)](https://plugins.gradle.org/plugin/org.panteleyev.jpackageplugin)
[![Gradle](https://img.shields.io/badge/Gradle-7.4%2B-green)](https://gradle.org/)
[![Java](https://img.shields.io/badge/Java-8-orange?logo=java)](https://www.oracle.com/java/technologies/javase-downloads.html)
[![GitHub](https://img.shields.io/github/license/petr-panteleyev/jpackage-gradle-plugin)](LICENSE)

## Finding jpackage

Plugin searches for ```jpackage``` executable using the following priority list:

1. Configured toolchain

2. ```java.home``` system property.

## Configuration

There are generic ```jpackage``` parameters as well as OS-specific parameters for OS X, Linux, Windows.
OS-specific parameters are processed when build is done on the corresponding OS.

If some generic parameters should have different values based on OS then they should be placed into configuration blocks:

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
    icon = "icons/icons.icns"
}

windows {
    // Generic parameter value for Windows build
    icon = "icons/icons.ico"
}
```

### Parameters

<table>
<tr><th>Parameter</th><th>Type</th><th>JPackage Argument</th><th>Min Version</th><th>Max Version</th></tr>
<tr><th colspan="5">Generic</th></tr>
<tr><td>aboutUrl</td><td>String</td><td>--about-url &lt;url></td><td>17</td><td>*</td></tr>
<tr><td>addModules</td><td>List&lt;String></td><td>--add-modules &lt;module>[,&lt;module>]</td><td>14</td><td>*</td></tr>
<tr><td>appDescription</td><td>String</td><td>--description &lt;description string></td><td>14</td><td>*</td></tr>
<tr><td>appContent</td><td>List&lt;String> <sup>(*)</sup></td><td>--app-content additional-content[,additional-content...]</td><td>18</td><td>*</td></tr>
<tr><td>appImage</td><td>String <sup>(*)</sup></td><td>--app-image &lt;name></td><td>14</td><td>*</td></tr>
<tr><td>appName</td><td>String</td><td>--name &lt;name></td><td>14</td><td>*</td></tr>
<tr><td>appVersion</td><td>String</td><td>--app-version &lt;version></td><td>14</td><td>*</td></tr>
<tr><td>arguments</td><td>List&lt;String></td><td>--arguments &lt;main class arguments></td><td>14</td><td>*</td></tr>
<tr><td>bindServices</td><td>Boolean</td><td>--bind-services</td><td>14</td><td>15</td></tr>
<tr><td>copyright</td><td>String</td><td>--copyright &lt;copyright string></td><td>14</td><td>*</td></tr>
<tr><td>destination</td><td>String <sup>(*)</sup></td><td>--dest &lt;destination path></td><td>14</td><td>*</td></tr>
<tr><td>fileAssociations</td><td>List&lt;String> <sup>(*)</sup></td><td>--file-associations &lt;file association property file></td><td>14</td><td>*</td></tr>
<tr><td>icon</td><td>String <sup>(*)</sup></td><td>--icon &lt;icon file path></td><td>14</td><td>*</td></tr>
<tr><td>input</td><td>String <sup>(*)</sup></td><td>--input &lt;input path></td><td>14</td><td>*</td></tr>
<tr><td>installDir</td><td>String</td><td>--install-dir &lt;file path></td><td>14</td><td>*</td></tr>
<tr><td>javaOptions</td><td>List&lt;String></td><td>--java-options &lt;options></td><td>14</td><td>*</td></tr>
<tr><td>jLinkOptions</td><td>List&lt;String></td><td>--jlink-options &lt;options></td><td>16</td><td>*</td></tr>
<tr><td>launchers</td><td>List&lt;Launcher> <sup>(*)</sup></td><td>--add-launcher &lt;name>=&lt;property file></td><td>14</td><td>*</td></tr>
<tr><td>launcherAsService</td><td>Boolean</td><td>--launcher-as-service</td><td>19</td><td>*</td></tr>
<tr><td>licenseFile</td><td>String <sup>(*)</sup></td><td>--license-file &lt;license file path></td><td>14</td><td>*</td></tr>
<tr><td>mainClass</td><td>String</td><td>--main-class &lt;class name></td><td>14</td><td>*</td></tr>
<tr><td>mainJar</td><td>String</td><td>--main-jar &lt;main jar file></td><td>14</td><td>*</td></tr>
<tr><td>module</td><td>String</td><td>--module &lt;module name>[/&lt;main class>]</td><td>14</td><td>*</td></tr>
<tr><td>modulePaths</td><td>List&lt;String> <sup>(*)</sup></td><td>--module-path &lt;module path></td><td>14</td><td>*</td></tr>
<tr><td>resourceDir</td><td>String <sup>(*)</sup></td><td>--resource-dir &lt;resource dir path></td><td>14</td><td>*</td></tr>
<tr><td>runtimeImage</td><td>String <sup>(*)</sup></td><td>--runtime-image &lt;file path></td><td>14</td><td>*</td></tr>
<tr><td>temp</td><td>String <sup>(*)</sup></td><td>--temp &lt;temp dir path></td><td>14</td><td>*</td></tr>
<tr><td>type</td><td>ImageType</td><td>--type &lt;type></td><td>14</td><td>*</td></tr>
<tr><td>vendor</td><td>String</td><td>--vendor &lt;vendor string></td><td>14</td><td>*</td></tr>
<tr><td>verbose</td><td>Boolean</td><td>--verbose</td><td>14</td><td>*</td></tr>

<tr><th colspan="5">Windows</th></tr>
<tr><td>winConsole</td><td>Boolean</td><td>--win-console</td><td>14</td><td>*</td></tr>
<tr><td>winDirChooser</td><td>Boolean</td><td>--win-dir-chooser</td><td>14</td><td>*</td></tr>
<tr><td>winHelpUrl</td><td>String</td><td>--win-help-url &lt;url></td><td>17</td><td>*</td></tr>
<tr><td>winMenu</td><td>Boolean</td><td>--win-menu</td><td>14</td><td>*</td></tr>
<tr><td>winMenuGroup</td><td>String</td><td>--win-menu-group &lt;menu group name></td><td>14</td><td>*</td></tr>
<tr><td>winPerUserInstall</td><td>Boolean</td><td>--win-per-user-install</td><td>14</td><td>*</td></tr>
<tr><td>winShortcut</td><td>Boolean</td><td>--win-shortcut</td><td>14</td><td>*</td></tr>
<tr><td>winShortcutPrompt</td><td>Boolean</td><td>--win-shortcut-prompt</td><td>17</td><td>*</td></tr>
<tr><td>winUpdateUrl</td><td>String</td><td>--win-update-url &lt;url></td><td>17</td><td>*</td></tr>
<tr><td>winUpgradeUuid</td><td>String</td><td>--win-upgrade-uuid &lt;id string></td><td>14</td><td>*</td></tr>

<tr><th colspan="5">OS X</th></tr>
<tr><td>macAppCategory</td><td>String</td><td>-mac-app-category &lt;category string></td><td>17</td><td>*</td></tr>
<tr><td>macAppStore</td><td>Boolean</td><td>--mac-app-store</td><td>17</td><td>*</td></tr>
<tr><td>macBundleSigningPrefix</td><td>String</td><td>--mac-bundle-signing-prefix &lt;prefix string></td><td>14</td><td>16</td></tr>
<tr><td>macDmgContent</td><td>List&lt;String> <sup>(*)</sup></td><td>--mac-dmg-content additional-content[,additional-content...]</td><td>18</td><td>*</td></tr>
<tr><td>macEntitlements</td><td>String <sup>(*)</sup></td><td>--mac-entitlements &lt;file path></td><td>17</td><td>*</td></tr>
<tr><td>macPackageIdentifier</td><td>String</td><td>--mac-package-identifier &lt;ID string></td><td>14</td><td>*</td></tr>
<tr><td>macPackageName</td><td>String</td><td>--mac-package-name &lt;name string></td><td>14</td><td>*</td></tr>
<tr><td>macPackageSigningPrefix</td><td>String</td><td>--mac-package-signing-prefix &lt;prefix string></td><td>17</td><td>*</td></tr>
<tr><td>macSign</td><td>Boolean</td><td>--mac-sign</td><td>14</td><td>*</td></tr>
<tr><td>macSigningKeychain</td><td>String <sup>(*)</sup></td><td>--mac-signing-keychain &lt;file path></td><td>14</td><td>*</td></tr>
<tr><td>macSigningKeyUserName</td><td>String</td><td>--mac-signing-key-user-name &lt;team name></td><td>14</td><td>*</td></tr>

<tr><th colspan="5">Linux</th></tr>
<tr><td>linuxAppCategory</td><td>String</td><td>--linux-app-category &lt;category value></td><td>14</td><td>*</td></tr>
<tr><td>linuxAppRelease</td><td>String</td><td>--linux-app-release &lt;release value></td><td>14</td><td>*</td></tr>
<tr><td>linuxDebMaintainer</td><td>String</td><td>--linux-deb-maintainer &lt;email address></td><td>14</td><td>*</td></tr>
<tr><td>linuxMenuGroup</td><td>String</td><td>--linux-menu-group &lt;menu-group-name></td><td>14</td><td>*</td></tr>
<tr><td>linuxPackageName</td><td>String</td><td>--linux-package-name &lt;package name></td><td>14</td><td>*</td></tr>
<tr><td>linuxPackageDeps</td><td>Boolean</td><td>--linux-package-deps</td><td>14</td><td>*</td></tr>
<tr><td>linuxRpmLicenseType</td><td>String</td><td>--linux-rpm-license-type &lt;type string></td><td>14</td><td>*</td></tr>
<tr><td>linuxShortcut</td><td>Boolean</td><td>--linux-shortcut</td><td>14</td><td>*</td></tr>

</table>

<sup>(*)</sup> - these parameters represent file or directory path and are resolved relative to the project root unless
they contain an absolute path.

### Image Type

|Plugin Value|JPackage Type|
|---|---|
|DEFAULT|Default image type, OS specific|
|APP_IMAGE|app-image|
|DMG|dmg|
|PKG|pkg|
|EXE|exe|
|MSI|msi|
|RPM|rpm|
|DEB|deb|

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

This plugin is not compatible with Gradle [configuration cache](https://docs.gradle.org/current/userguide/configuration_cache.html).

## Examples

* [Modular Application with Full Runtime](doc/examples/ModularFullRuntime.md)
* [Non-Modular Application](doc/examples/Non-ModularApplication.md)

## References

[Packaging Tool User's Guide](https://docs.oracle.com/en/java/javase/19/jpackage/packaging-tool-user-guide.pdf)

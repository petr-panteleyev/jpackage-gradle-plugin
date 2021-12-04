# JPackage Gradle Plugin

Gradle plugin for [jpackage](https://openjdk.java.net/jeps/343) tool available since JDK-14.

[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/org/panteleyev/jpackageplugin/org.panteleyev.jpackageplugin.gradle.plugin/maven-metadata.xml.svg?label=Gradle%20Plugin)](https://plugins.gradle.org/plugin/org.panteleyev.jpackageplugin)
[![Gradle](https://img.shields.io/badge/Gradle-6.7%2B-green)](https://gradle.org/)
[![Java](https://img.shields.io/badge/Java-8-orange?logo=java)](https://www.oracle.com/java/technologies/javase-downloads.html)
[![BSD-2 license](https://img.shields.io/badge/License-BSD--2-informational.svg)](LICENSE)

## Getting started

Add to your `build.gradle`:

```gradle
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "org.panteleyev:jpackage-gradle-plugin:1.3.1"
  }
}

apply plugin: "org.panteleyev.jpackageplugin"

tasks.jpackage {
    dependsOn("dist")

    input  = "${buildDir}/libs"
    destination = "${buildDir}/package"
    mainJar = "your-file-name.jar"
    mainClass = "com.example.YourMainClass"
    type = "app-image" // {"app-image", "exe", "msi", "rpm", "deb", "pkg", "dmg"}

    windows {
        winConsole = true // otherwise errors will just silently throw a "Failed to launch JVM" error
    }
}
```

You should then be able to create a package using `gradle jpackage` (or `gradlew jpackage` on Windows). Use the `--info` flag to see what's happening. 

More examples are listed in [doc/examples/](doc/examples/).

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
<tr><th>Parameter</th><th>Type</th><th>JPackage Argument</th></tr>
<tr><th colspan="3">Generic</th></tr>
<tr><td>type</td><td>ImageType</td><td>--type &lt;type></td></tr>
<tr><td>appName</td><td>String</td><td>--name &lt;name></td></tr>
<tr><td>appVersion</td><td>String</td><td>--app-version &lt;version></td></tr>
<tr><td>copyright</td><td>String</td><td>--copyright &lt;copyright string></td></tr>
<tr><td>appDescription</td><td>String</td><td>--description &lt;description string></td></tr>
<tr><td>vendor</td><td>String</td><td>--vendor &lt;vendor string></td></tr>
<tr><td>installDir</td><td>String</td><td>--install-dir &lt;file path></td></tr>
<tr><td>module</td><td>String</td><td>--module &lt;module name>[/&lt;main class>]</td></tr>
<tr><td>mainClass</td><td>String</td><td>--main-class &lt;class name></td></tr>
<tr><td>mainJar</td><td>String</td><td>--main-jar &lt;main jar file></td></tr>
<tr><td>verbose</td><td>String</td><td>--verbose</td></tr>
<tr><td>arguments</td><td>String</td><td>--arguments &lt;main class arguments></td></tr>
<tr><td>addModules</td><td>String</td><td>--add-modules &lt;module>[,&lt;module>]</td></tr>
<tr><td>appImage</td><td>String <sup>(*)</sup></td><td>--app-image &lt;name></td></tr>
<tr><td>destination</td><td>String <sup>(*)</sup></td><td>--dest &lt;destination path></td></tr>
<tr><td>fileAssociations</td><td>String <sup>(*)</sup></td><td>--file-associations &lt;file association property file></td></tr>
<tr><td>icon</td><td>String <sup>(*)</sup></td><td>--icon &lt;icon file path></td></tr>
<tr><td>input</td><td>String <sup>(*)</sup></td><td>--input &lt;input path></td></tr>
<tr><td>licenseFile</td><td>String <sup>(*)</sup></td><td>--license-file &lt;license file path></td></tr>
<tr><td>modulePaths</td><td>String <sup>(*)</sup></td><td>--module-path &lt;module path></td></tr>
<tr><td>resourceDir</td><td>String <sup>(*)</sup></td><td>--resource-dir &lt;resource dir path></td></tr>
<tr><td>runtimeImage</td><td>String <sup>(*)</sup></td><td>--runtime-image &lt;file path></td></tr>
<tr><td>temp</td><td>String <sup>(*)</sup></td><td>--temp &lt;temp dir path></td></tr>
<tr><td>launchers</td><td>Launcher <sup>(*)</sup></td><td>--add-launcher &lt;name>=&lt;property file></td></tr>

<tr><th colspan="3">Windows</th></tr>
<tr><td>winMenu</td><td>Boolean</td><td>--win-menu</td></tr>
<tr><td>winDirChooser</td><td>Boolean</td><td>--win-dir-chooser</td></tr>
<tr><td>winUpgradeUuid</td><td>String</td><td>--win-upgrade-uuid &lt;id string></td></tr>
<tr><td>winMenuGroup</td><td>String</td><td>--win-menu-group &lt;menu group name></td></tr>
<tr><td>winShortcut</td><td>Boolean</td><td>--win-shortcut</td></tr>
<tr><td>winPerUserInstall</td><td>Boolean</td><td>--win-per-user-install</td></tr>
<tr><td>winConsole</td><td>Boolean</td><td>--win-console</td></tr>

<tr><th colspan="3">OS X</th></tr>
<tr><td>macPackageIdentifier</td><td>String</td><td>--mac-package-identifier &lt;ID string></td></tr>
<tr><td>macPackageName</td><td>String</td><td>--mac-package-name &lt;name string></td></tr>
<tr><td>macPackageSigningPrefix</td><td>String</td><td>--mac-package-signing-prefix &lt;prefix string></td></tr>
<tr><td>macSign</td><td>Boolean</td><td>--mac-sign</td></tr>
<tr><td>macSigningKeychain</td><td>String <sup>(*)</sup></td><td>--mac-signing-keychain &lt;file path></td></tr>
<tr><td>macSigningKeyUserName</td><td>String</td><td>--mac-signing-key-user-name &lt;team name></td></tr>

<tr><th colspan="3">Linux</th></tr>
<tr><td>linuxPackageName</td><td>String</td><td>--linux-package-name &lt;package name></td></tr>
<tr><td>linuxDebMaintainer</td><td>String</td><td>--linux-deb-maintainer &lt;email address></td></tr>
<tr><td>linuxMenuGroup</td><td>String</td><td>--linux-menu-group &lt;menu-group-name></td></tr>
<tr><td>linuxRpmLicenseType</td><td>String</td><td>--linux-rpm-license-type &lt;type string></td></tr>
<tr><td>linuxAppRelease</td><td>String</td><td>--linux-app-release &lt;release value></td></tr>
<tr><td>linuxAppCategory</td><td>String</td><td>--linux-app-category &lt;category value></td></tr>
<tr><td>linuxShortcut</td><td>Boolean</td><td>--linux-shortcut</td></tr>

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
Each argument should be specified using &lt;argument> configuration parameter.

_Example:_

```kotlin
argumens = listOf(
    "SomeArgument",
    "Argument with spaces",
    "Argument with \"quotes\""
)
```

### Additional Parameters

Additional parameters allow passing ```jpackage``` command line options not supported by the plugin. These parameters
are passed as is without any transformation.

_Example:_

```kotlin
additionalParameters = listOf(
    "--jlink-options",
    "--bind-services"
)
```

## Logging

Plugin uses ```LogLevel.INFO``` to print various information about toolchain, jpackage parameters, etc. Use gradle 
option ```--info``` to check this output.

## Dry Run Mode

To execute plugin tasks in dry run mode without calling ```jpackage``` set property```jpackage.dryRun``` to true.

_Example:_

```shell
$ ./gradlew clean build jpackage --info -Djpackage.dryRun=true
```

## Examples

* [Modular Application with Full Runtime](doc/examples/ModularFullRuntime.md)
* [Non-Modular Application](doc/examples/Non-ModularApplication.md)

## References

[Packaging Tool User's Guide](https://docs.oracle.com/en/java/javase/16/jpackage/packaging-tool-user-guide.pdf)

# JPackage Gradle Plugin

Gradle plugin for [jpackage](https://openjdk.java.net/jeps/343) tool available in JDK-14.

[![BSD-2 license](https://img.shields.io/badge/License-BSD--2-informational.svg)](LICENSE)

## Usage

```kotlin
plugins {
    id("org.panteleyev.jpackageplugin") version "0.0.1"
}
```
This plugin will try to use ```jpackage``` executable from path specified by ```java.home``` system property.

## Configuration

There are generic parameters as well as OS-specific parameters for OS X, Linux, Windows.
Plugin determines OS name using ```os.name``` system property in order to configure OS-specific parameters.

OS-specific parameters should be conditionally specified for each required OS.

*Example:*

```kotlin
if (OperatingSystem.current().isMacOsX) {
    icon = "icons/icons.icns"
} else if (OperatingSystem.current().isWindows) {
    icon = "icons/icons.ico"
    winMenu = true
    winDirChooser = true
}
```

### Generic Parameters

| Parameter | JPackage Argument |
|---|---|
|type|--type &lt;type>|
|appName|--name &lt;name>|
|appVersion|--app-version &lt;version>|
|destination|--dest &lt;destination path>|
|copyright|--copyright &lt;copyright string>|
|appDescription|--description &lt;description string>|
|vendor|--vendor &lt;vendor string>|
|runtimeImage|--runtime-image &lt;file path>|
|input|--input &lt;input path>|
|installDir|--install-dir &lt;file path>|
|module|--module &lt;module name>[/&lt;main class>]|
|modulePath|--module-path &lt;module path>...|
|mainClass|--main-class &lt;class name>|
|mainJar|--main-jar &lt;main jar file>|
|icon|--icon &lt;icon file path>|
|verbose|--verbose|
|arguments|--arguments &lt;main class arguments>|

### Windows Specific Parameters

| Parameter | jpackage argument |
|---|---|
|winMenu|--win-menu|
|winDirChooser|--win-dir-chooser|
|winUpgradeUuid|--win-upgrade-uuid &lt;id string>|
|winMenuGroup|--win-menu-group &lt;menu group name>|
|winShortcut|--win-shortcut|
|winPerUserInstall|--win-per-user-install|

### OS X Specific Parameters

| Parameter | jpackage argument |
|---|---|
|macPackageIdentifier|--mac-package-identifier &lt;ID string>|
|macPackageName|--mac-package-name &lt;name string>|
|macPackageSigningPrefix|--mac-package-signing-prefix &lt;prefix string>|
|macSign|--mac-sign|
|macSigningKeychain|--mac-signing-keychain &lt;file path>|
|macSigningKeyUserName|--mac-signing-key-user-name &lt;team name>|

### Linux Specific Parameters

| Parameter | jpackage argument |
|---|---|
|linuxPackageName|--linux-package-name &lt;package name>|
|linuxDebMaintainer|--linux-deb-maintainer &lt;email address>|
|linuxMenuGroup|--linux-menu-group &lt;menu-group-name>|
|linuxRpmLicenseType|--linux-rpm-license-type &lt;type string>|
|linuxAppRelease|--linux-app-release &lt;release value>|
|linuxAppCategory|--linux-app-category &lt;category value>|
|linuxShortcut|--linux-shortcut|

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

## Samples

### Application image with full JRE

```kotlin
task("copyDependencies", Copy::class) {
    from(configurations.runtimeClasspath).into("$buildDir/jmods")
}

tasks.withType<org.panteleyev.jpackage.JPackageTask> {
    dependsOn("build")
    dependsOn("copyDependencies")

    appName = "Application Name"
    appVersion = project.version as String
    vendor = "app.org"
    copyright = "Copyright (c) 2020 Vendor"
    runtimeImage = System.getProperty("java.home")
    module = "org.app.module/org.app.MainClass"
    modulePath = "$buildDir/jmods"
    destination = "$buildDir/dist"
    javaOptions = listOf(
        "--enable-preview",
        "-Dfile.encoding=UTF-8"
    )

    if (OperatingSystem.current().isMacOsX) {
        icon = "icons/icons.icns"
    } else if (OperatingSystem.current().isWindows) {
        icon = "icons/icons.ico"
        winMenu = true
        winDirChooser = true
    }
}
```

## References

[Packaging Tool User's Guide](https://docs.oracle.com/en/java/javase/14/jpackage/packaging-tool-user-guide.pdf)
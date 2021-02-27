# JPackage Gradle Plugin

Gradle plugin for [jpackage](https://openjdk.java.net/jeps/343) tool available since JDK-14.

[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/org/panteleyev/jpackageplugin/org.panteleyev.jpackageplugin.gradle.plugin/maven-metadata.xml.svg?label=Gradle%20Plugin)](https://plugins.gradle.org/plugin/org.panteleyev.jpackageplugin)
[![Gradle](https://img.shields.io/badge/Gradle-6.7%2B-green)](https://gradle.org/)
[![BSD-2 license](https://img.shields.io/badge/License-BSD--2-informational.svg)](LICENSE)

## Finding jpackage

Plugin searches for jpackage executable using the following priority list:

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
<tr><th>Parameter</th><th>JPackage Argument</th></tr>
<tr><th colspan="2">Generic</th></tr>
<tr><td>type</td><td>--type &lt;type></td></tr>
<tr><td>appName</td><td>--name &lt;name></td></tr>
<tr><td>appVersion</td><td>--app-version &lt;version></td></tr>
<tr><td>destination</td><td>--dest &lt;destination path></td></tr>
<tr><td>copyright</td><td>--copyright &lt;copyright string></td></tr>
<tr><td>appDescription</td><td>--description &lt;description string></td></tr>
<tr><td>vendor</td><td>--vendor &lt;vendor string></td></tr>
<tr><td>runtimeImage</td><td>--runtime-image &lt;file path></td></tr>
<tr><td>input</td><td>--input &lt;input path></td></tr>
<tr><td>installDir</td><td>--install-dir &lt;file path></td></tr>
<tr><td>module</td><td>--module &lt;module name>[/&lt;main class>]</td></tr>
<tr><td>modulePaths</td><td>--module-path &lt;module path>...</td></tr>
<tr><td>mainClass</td><td>--main-class &lt;class name></td></tr>
<tr><td>mainJar</td><td>--main-jar &lt;main jar file></td></tr>
<tr><td>icon</td><td>--icon &lt;icon file path></td></tr>
<tr><td>verbose</td><td>--verbose</td></tr>
<tr><td>arguments</td><td>--arguments &lt;main class arguments></td></tr>
<tr><td>licenseFile</td><td>--license-file &lt;license file path></td></tr>
<tr><td>resourceDir</td><td>--resource-dir &lt;resource dir path></td></tr>
<tr><td>temp</td><td>--temp &lt;temp dir path></td></tr>
<tr><td>fileAssociations</td><td>--file-associations &lt;file association property file></td></tr>
<tr><td>launchers</td><td>--add-launcher &lt;launcher property file></td></tr>
<tr><td>addModules</td><td>--add-modules &lt;module>[,&lt;module>]</td></tr>

<tr><th colspan="2">Windows</th></tr>
<tr><td>winMenu</td><td>--win-menu</td></tr>
<tr><td>winDirChooser</td><td>--win-dir-chooser</td></tr>
<tr><td>winUpgradeUuid</td><td>--win-upgrade-uuid &lt;id string></td></tr>
<tr><td>winMenuGroup</td><td>--win-menu-group &lt;menu group name></td></tr>
<tr><td>winShortcut</td><td>--win-shortcut</td></tr>
<tr><td>winPerUserInstall</td><td>--win-per-user-install</td></tr>

<tr><th colspan="2">OS X</th></tr>
<tr><td>macPackageIdentifier</td><td>--mac-package-identifier &lt;ID string></td></tr>
<tr><td>macPackageName</td><td>--mac-package-name &lt;name string></td></tr>
<tr><td>macPackageSigningPrefix</td><td>--mac-package-signing-prefix &lt;prefix string></td></tr>
<tr><td>macSign</td><td>--mac-sign</td></tr>
<tr><td>macSigningKeychain</td><td>--mac-signing-keychain &lt;file path></td></tr>
<tr><td>macSigningKeyUserName</td><td>--mac-signing-key-user-name &lt;team name></td></tr>

<tr><th colspan="2">Linux</th></tr>
<tr><td>linuxPackageName</td><td>--linux-package-name &lt;package name></td></tr>
<tr><td>linuxDebMaintainer</td><td>--linux-deb-maintainer &lt;email address></td></tr>
<tr><td>linuxMenuGroup</td><td>--linux-menu-group &lt;menu-group-name></td></tr>
<tr><td>linuxRpmLicenseType</td><td>--linux-rpm-license-type &lt;type string></td></tr>
<tr><td>linuxAppRelease</td><td>--linux-app-release &lt;release value></td></tr>
<tr><td>linuxAppCategory</td><td>--linux-app-category &lt;category value></td></tr>
<tr><td>linuxShortcut</td><td>--linux-shortcut</td></tr>

</table>

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

task("copyJar", Copy::class) {
    from(tasks.jar).into("$buildDir/jmods")
}

tasks.withType<org.panteleyev.jpackage.JPackageTask> {
    dependsOn("build")
    dependsOn("copyDependencies")
    dependsOn("copyJar")

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

    mac {
        icon = "icons/icons.icns"
    }
    
    windows {
        icon = "icons/icons.ico"
        winMenu = true
        winDirChooser = true
    }
}
```

## Gradle Version Compatibility

| Plugin | Gradle |
|---|---|
|0.0.3+|6.7+|

## References

[Packaging Tool User's Guide](https://docs.oracle.com/en/java/javase/15/jpackage/packaging-tool-user-guide.pdf)

/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
group = "org.panteleyev"
version = "1.3.1"

plugins {
    java
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.20.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.testng:testng:7.3.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

gradlePlugin {
    val jpackage by plugins.creating {
        id = "org.panteleyev.jpackageplugin"
        version = project.version
        displayName = "JPackage Gradle Plugin"
        description = "A plugin that executes jpackage tool from JDK-14+"
        implementationClass = "org.panteleyev.jpackage.JPackageGradlePlugin"
    }
}

pluginBundle {
    website = "https://github.com/petr-panteleyev/jpackage-gradle-plugin"
    vcsUrl = "https://github.com/petr-panteleyev/jpackage-gradle-plugin.git"
    tags = listOf("jpackage")
}

tasks.withType<Test> {
    useTestNG()
}

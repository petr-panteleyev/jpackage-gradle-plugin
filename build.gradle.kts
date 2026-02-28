// Copyright © 2020-2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
group = "org.panteleyev"
version = "2.0.1"

plugins {
    id("com.gradle.plugin-publish") version "2.0.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(junit.jupiter)
    testRuntimeOnly(junit.platform.launcher)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

gradlePlugin {
    website = "https://github.com/petr-panteleyev/jpackage-gradle-plugin"
    vcsUrl = "https://github.com/petr-panteleyev/jpackage-gradle-plugin.git"
    plugins {
        register("jpackageplugin") {
            id = "org.panteleyev.jpackageplugin"
            version = project.version
            displayName = "JPackage Gradle Plugin"
            description = "A plugin that executes jpackage tool from JDK-14+"
            implementationClass = "org.panteleyev.jpackage.JPackageGradlePlugin"
            tags = listOf("jpackage")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

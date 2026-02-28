// Copyright © 2020-2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause

dependencyResolutionManagement {
    versionCatalogs {
        create("junit") {
            var version = "6.0.3"
            library("jupiter", "org.junit.jupiter:junit-jupiter:$version")
            library("platform-launcher", "org.junit.platform:junit-platform-launcher:$version")
        }
    }
}

rootProject.name = "jpackage-gradle-plugin"

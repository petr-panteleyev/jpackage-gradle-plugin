/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage

import org.gradle.api.Project
import org.gradle.api.Plugin

/**
 * JPackage Gradle plugin.
 */
class JPackageGradlePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("jpackage", JPackageTask::class.java);
    }
}

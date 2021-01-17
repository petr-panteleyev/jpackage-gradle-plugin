/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * JPackage Gradle Plugin.
 */
public class JPackageGradlePlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getTasks().register("jpackage", JPackageTask.class);
    }
}

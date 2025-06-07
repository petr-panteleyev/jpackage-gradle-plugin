/*
 Copyright © 2021-2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * JPackage Gradle Plugin.
 */
public class JPackageGradlePlugin implements Plugin<Project> {
    private static final String GROUP = "Distribution";
    private static final String DESCRIPTION = "Creates application bundle using jpackage.";

    @Override
    public void apply(Project target) {
        target.getTasks().register("jpackage", JPackageTask.class, task -> {
            task.setGroup(GROUP);
            task.setDescription(DESCRIPTION);
        });
    }
}

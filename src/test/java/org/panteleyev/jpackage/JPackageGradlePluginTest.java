// Copyright © 2022-2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.jpackage;

import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JPackageGradlePluginTest {
    @Test
    public void pluginRegistersTask() {
        var project = ProjectBuilder.builder().build();
        project.getPlugins().apply("org.panteleyev.jpackageplugin");
        assertNotNull(project.getTasks().findByName("jpackage"));
    }
}

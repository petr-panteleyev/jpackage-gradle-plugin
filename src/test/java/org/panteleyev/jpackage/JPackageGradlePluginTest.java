/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.testng.annotations.Test;
import static org.testng.Assert.assertNotNull;

public class JPackageGradlePluginTest {
    @Test
    public void pluginRegistersTask() {
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("org.panteleyev.jpackageplugin");

        assertNotNull(project.getTasks().findByName("jpackage"));
    }
}

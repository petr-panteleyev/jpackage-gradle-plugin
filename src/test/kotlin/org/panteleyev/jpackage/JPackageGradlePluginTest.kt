/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage

import org.gradle.testfixtures.ProjectBuilder
import org.testng.Assert.assertNotNull
import org.testng.annotations.Test

class JPackageGradlePluginTest {
    @Test
    fun `plugin registers task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("org.panteleyev.jpackageplugin")

        assertNotNull(project.tasks.findByName("jpackage"))
    }
}

/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage;

import org.gradle.api.GradleException;
import java.io.File;

public class Launcher {
    private final String name;
    private final File file;

    public Launcher(String name, String filePath) {
        if (name == null || name.isEmpty() || filePath == null || filePath.isEmpty()) {
            throw new GradleException("Launcher parameters cannot be null or empty");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            throw new GradleException("Launcher file " + file.getAbsolutePath() + " does not exist");
        }

        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }
}

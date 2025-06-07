/*
 Copyright © 2021-2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

import org.gradle.api.GradleException;

import java.io.File;
import java.util.Objects;

public final class Launcher {
    private final String name;
    private final String filePath;

    public Launcher(String name, String filePath) {
        if (name == null || name.isEmpty() || filePath == null) {
            throw new GradleException("Launcher parameters cannot be null");
        }

        this.name = name;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, filePath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof Launcher) {
            Launcher that = (Launcher) o;
            return Objects.equals(this.name, that.name)
                && Objects.equals(this.filePath, that.filePath);
        } else {
            return false;
        }
    }
}

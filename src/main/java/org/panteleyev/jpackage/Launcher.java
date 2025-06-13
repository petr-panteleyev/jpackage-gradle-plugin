/*
 Copyright © 2021-2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

import org.gradle.api.GradleException;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public final class Launcher implements Serializable {
    private static final long serialVersionUID = 202506132253L;

    private final String name;
    private final File file;

    public Launcher(String name, File file) {
        if (name == null || name.isEmpty() || file == null) {
            throw new GradleException("Launcher parameters cannot be null ");
        }

        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof Launcher) {
            Launcher that = (Launcher) o;
            return Objects.equals(this.name, that.name)
                && Objects.equals(this.file, that.file);
        } else {
            return false;
        }
    }
}

// Copyright © 2021-2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.jpackage;

import org.gradle.api.GradleException;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

public record Launcher(String name, File file) implements Serializable {
    @Serial
    private static final long serialVersionUID = 202506132253L;

    public Launcher {
        if (name == null || name.isEmpty() || file == null) {
            throw new GradleException("Launcher parameters cannot be null ");
        }
    }
}

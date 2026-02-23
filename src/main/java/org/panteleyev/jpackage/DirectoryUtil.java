// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.jpackage;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

final class DirectoryUtil {

    static boolean isNestedDirectory(Path parent, Path child) {
        var absoluteParent = parent.toAbsolutePath();
        var absoluteChild = child.toAbsolutePath();
        return absoluteChild.startsWith(absoluteParent);
    }

    static void removeDirectory(Path dir) {
        if (!dir.toFile().exists()) return;

        try (var paths = Files.walk(dir)) {
            paths.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private DirectoryUtil() {
    }
}

// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.jpackage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.Comparator;

import static org.panteleyev.jpackage.OsUtil.isWindows;

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
                    .forEach(DirectoryUtil::delete);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static void delete(Path path) {
        try {
            if (isWindows()) {
                clearDosReadonly(path);
            }
            Files.delete(path);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static void clearDosReadonly(Path path) throws IOException {
        var view = Files.getFileAttributeView(path, DosFileAttributeView.class);
        if (view != null) {
            view.setReadOnly(false);
        }
    }

    private DirectoryUtil() {
    }
}

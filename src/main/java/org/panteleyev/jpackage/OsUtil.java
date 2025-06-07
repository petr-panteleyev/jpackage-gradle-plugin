/*
 Copyright © 2021-2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

import static org.gradle.internal.os.OperatingSystem.current;

abstract class OsUtil {
    static boolean isWindows() {
        return current().isWindows();
    }

    static boolean isMac() {
        return current().isMacOsX();
    }

    static boolean isLinux() {
        return current().isLinux();
    }
}

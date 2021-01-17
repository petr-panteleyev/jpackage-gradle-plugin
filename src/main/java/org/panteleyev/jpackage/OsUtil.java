/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage;

import org.gradle.internal.os.OperatingSystem;

abstract class OsUtil {
    static boolean isWindows() {
        return OperatingSystem.current().isWindows();
    }

    static boolean isMac() {
        return OperatingSystem.current().isMacOsX();
    }

    static boolean isLinux() {
        return OperatingSystem.current().isLinux();
    }
}

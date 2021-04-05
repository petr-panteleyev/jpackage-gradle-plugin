/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
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

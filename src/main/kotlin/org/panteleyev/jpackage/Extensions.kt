/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage

import org.gradle.internal.os.OperatingSystem
import java.util.regex.Matcher

private val REPLACER = Matcher.quoteReplacement(if (OperatingSystem.current().isWindows) "\\\\\\\"" else "\\\"")
private val SPACE_WRAPPER = if (OperatingSystem.current().isWindows) "\\\"" else "\""

internal fun String.escape() : String {
    var result = this.replace("\"".toRegex(), REPLACER)
    if (result.contains(" ")) {
        result = SPACE_WRAPPER + result + SPACE_WRAPPER
    }
    return result
}

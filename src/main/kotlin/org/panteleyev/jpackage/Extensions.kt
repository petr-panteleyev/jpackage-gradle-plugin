/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage

import org.gradle.internal.os.OperatingSystem
import java.util.regex.Matcher

internal fun ArrayList<String>.addParameter(name: String, value: String): ArrayList<String> {
    if (value.isBlank()) {
        return this
    }

    println("$name $value")
    add(name)
    add(value)
    return this
}

internal fun ArrayList<String>.addParameter(name: String, value: Boolean): ArrayList<String> {
    if (value) {
        println(name)
        add(name)
    }

    return this
}

internal fun ArrayList<String>.addParameter(name: String, value: EnumParameter?): ArrayList<String> {
    if (value != null) {
        addParameter(name, value.value)
    }
    return this
}

private val REPLACER = Matcher.quoteReplacement(if (OperatingSystem.current().isWindows) "\\\\\\\"" else "\\\"")
private val SPACE_WRAPPER = if (OperatingSystem.current().isWindows) "\\\"" else "\""

internal fun String.escape() : String {
    var result = this.replace("\"".toRegex(), REPLACER)
    if (result.contains(" ")) {
        result = SPACE_WRAPPER + result + SPACE_WRAPPER
    }
    return result
}

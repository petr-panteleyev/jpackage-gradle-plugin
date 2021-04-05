/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage;

import java.util.regex.Matcher;
import static org.panteleyev.jpackage.OsUtil.isWindows;

abstract class StringUtil {
    private static final String REPLACER = Matcher.quoteReplacement(isWindows() ? "\\\\\\\"" : "\\\"");
    private static final String SPACE_WRAPPER = isWindows() ? "\\\"" : "\"";

    static String escape(String arg) {
        arg = arg.replaceAll("\"", REPLACER);
        return arg.contains(" ") ? SPACE_WRAPPER + arg + SPACE_WRAPPER : arg;
    }
}

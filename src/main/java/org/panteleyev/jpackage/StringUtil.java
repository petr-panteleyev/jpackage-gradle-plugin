/*
 Copyright © 2021-2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
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

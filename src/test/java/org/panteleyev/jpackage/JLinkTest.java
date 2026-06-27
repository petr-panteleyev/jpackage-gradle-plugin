// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.jpackage;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

public class JLinkTest {
    private static List<Arguments> arguments() {
        return Arrays.asList(
                argumentSet("Empty", new JLink(), ""),
                argumentSet("Single", new JLink(true, false, false, false, false, false), "--bind-services"),
                argumentSet("Several", new JLink(true, false, true, false, false, false),
                        "--bind-services --no-man-pages"),
                argumentSet("All", new JLink(true, true, true, true, true, true),
                        "--bind-services --no-header-files --no-man-pages --strip-debug --strip-native-commands --generate-cds-archive")
        );
    }

    @ParameterizedTest
    @MethodSource("arguments")
    public void test(JLink jLink, String expected) {
        assertEquals(expected, jLink.build());
    }
}

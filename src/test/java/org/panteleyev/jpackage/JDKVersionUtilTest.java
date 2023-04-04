/*
 Copyright © 2023 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JDKVersionUtilTest {

    private static List<Arguments> javaCmdArguments() {
        return Arrays.asList(
                Arguments.of("/opt/jdk-15.0.2/bin/jpackage", "/opt/jdk-15.0.2/bin/java"),
                Arguments.of("C:\\Program Files\\jdk-15.0.2\\bin\\jpackage.exe",
                        "C:\\Program Files\\jdk-15.0.2\\bin\\java.exe")
        );
    }

    @ParameterizedTest
    @MethodSource("javaCmdArguments")
    public void testBuildJavaExecutable(String given, String expected) {
        assertEquals(expected, JDKVersionUtil.buildJavaExecutable(given));
    }
}

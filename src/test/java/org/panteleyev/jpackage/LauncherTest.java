/*
 Copyright © 2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

import org.gradle.api.GradleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LauncherTest {

    private static List<Arguments> constructorExceptions() {
        return Arrays.asList(
                Arguments.of("", ""),
                Arguments.of(null, null),
                Arguments.of("", null),
                Arguments.of(null, "")
        );
    }

    @ParameterizedTest
    @MethodSource("constructorExceptions")
    public void testConstructorException(String name, String filePath) {
        assertThrows(GradleException.class, () -> {
            new Launcher(name, filePath);
        });
    }

    @Test
    public void testEquals() {
        Launcher l1 = new Launcher("123", "345");
        Launcher l2 = new Launcher("123", "345");
        Launcher l3 = new Launcher("345", "123");

        assertEquals(l1, l1);
        assertEquals(l2, l1);
        assertNotEquals(l3, l2);
    }
}

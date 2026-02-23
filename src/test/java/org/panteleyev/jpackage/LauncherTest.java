// Copyright © 2022-2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.jpackage;

import org.gradle.api.GradleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LauncherTest {

    private static List<Arguments> constructorExceptions() {
        return Arrays.asList(
                Arguments.of("", new File("")),
                Arguments.of(null, null),
                Arguments.of("", null),
                Arguments.of(null, new File(""))
        );
    }

    @ParameterizedTest
    @MethodSource("constructorExceptions")
    public void testConstructorException(String name, File file) {
        assertThrows(GradleException.class, () -> new Launcher(name, file));
    }

    @Test
    public void testEquals() {
        var l1 = new Launcher("123", new File("345"));
        var l2 = new Launcher("123", new File("345"));
        var l3 = new Launcher("345", new File("345"));

        assertEquals(l1, l1);
        assertEquals(l2, l1);
        assertNotEquals(l3, l2);
    }
}

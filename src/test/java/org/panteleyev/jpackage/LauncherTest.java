/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage;

import org.gradle.api.GradleException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;

public class LauncherTest {

    @DataProvider(name = "constructorExceptions")
    private Object[][] dataProvider() {
        return new Object[][]{
            {"", ""},
            {null, null},
            {"", null},
            {null, ""}
        };
    }

    @Test(dataProvider = "constructorExceptions", expectedExceptions = GradleException.class)
    public void testConstructorException(String name, String filePath) {
        new Launcher(name, filePath);
    }

    @Test
    public void testEquals() {
        Launcher l1 = new Launcher("123", "345");
        Launcher l2 = new Launcher("123", "345");
        Launcher l3 = new Launcher("345", "123");

        assertEquals(l1, l1);
        assertEquals(l1, l2);
        assertNotEquals(l2, l3);
    }
}

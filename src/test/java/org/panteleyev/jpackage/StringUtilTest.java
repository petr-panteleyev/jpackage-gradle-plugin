/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.panteleyev.jpackage.OsUtil.isWindows;
import static org.panteleyev.jpackage.StringUtil.escape;
import static org.testng.Assert.assertEquals;

@Test
public class StringUtilTest {

    @DataProvider
    private Object[][] dataProvider() {
        if (isWindows()) {
            return new Object[][]{
                {"", ""},
                {"123", "123"},
                {"-DAppOption=text string", "\\\"-DAppOption=text string\\\""},
                {"-XX:OnError=\"userdump.exe %p\"", "\\\"-XX:OnError=\\\\\\\"userdump.exe %p\\\\\\\"\\\""},
            };
        } else {
            return new Object[][]{
                {"", ""},
                {"123", "123"},
                {"-DAppOption=text string", "\"-DAppOption=text string\""},
                {"-XX:OnError=\"userdump.exe %p\"", "\"-XX:OnError=\\\"userdump.exe %p\\\"\""},
            };
        }
    }

    @Test(dataProvider = "dataProvider")
    public void testEscape(String arg, String expected) {
        assertEquals(escape(arg), expected);
    }
}

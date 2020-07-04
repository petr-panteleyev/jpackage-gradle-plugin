/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage

import org.gradle.internal.os.OperatingSystem
import org.testng.Assert.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class ExtensionsTest {
    @DataProvider
    private fun dataProvider(): Array<Array<String>>? {
        return if (OperatingSystem.current().isWindows) {
            arrayOf(
                arrayOf("", ""),
                arrayOf("123", "123"),
                arrayOf("-DAppOption=text string", "\\\"-DAppOption=text string\\\""),
                arrayOf("-XX:OnError=\"userdump.exe %p\"", "\\\"-XX:OnError=\\\\\\\"userdump.exe %p\\\\\\\"\\\"")
            )
        } else {
            arrayOf(
                arrayOf("", ""),
                arrayOf("123", "123"),
                arrayOf("-DAppOption=text string", "\"-DAppOption=text string\""),
                arrayOf("-XX:OnError=\"userdump.exe %p\"", "\"-XX:OnError=\\\"userdump.exe %p\\\"\""))
        }
    }

    @Test(dataProvider = "dataProvider")
    fun testEscape(arg: String, expected: String?) {
        assertEquals(expected, arg.escape())
    }
}
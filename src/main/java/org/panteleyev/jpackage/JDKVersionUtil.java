/*
 Copyright © 2023 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static org.panteleyev.jpackage.JPackageTask.EXECUTABLE;

final class JDKVersionUtil {
    private static final String JAVA_VERSION_PATTERN = "java.version = ";

    public static int getJDKMajorVersion(String jpackageCmd) {
        String cmd = buildJavaExecutable(jpackageCmd);
        List<String> parameters = Arrays.asList(
                cmd.contains(" ") ? "\"" + cmd + "\"" : cmd,
                "-XshowSettings:properties",
                "-version"
        );

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            Process process = processBuilder
                    .redirectErrorStream(true)
                    .command(parameters)
                    .start();

            int result = 0;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    int index = line.indexOf(JAVA_VERSION_PATTERN);
                    if (index == -1) {
                        continue;
                    }
                    String[] parts = line.substring(index + JAVA_VERSION_PATTERN.length()).split("\\.");
                    result = Integer.parseInt(parts[0]);
                    break;
                }
            }

            int status = process.waitFor();
            if (status != 0) {
                return 0;
            } else {
                return result;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    static String buildJavaExecutable(String jpackageExecutable) {
        int index = jpackageExecutable.indexOf(EXECUTABLE);
        return jpackageExecutable.substring(0, index)
                + "java"
                + jpackageExecutable.substring(index + EXECUTABLE.length());
    }

    private JDKVersionUtil() {
    }
}

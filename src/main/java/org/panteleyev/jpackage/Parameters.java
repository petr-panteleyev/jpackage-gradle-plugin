/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

import org.gradle.api.GradleException;
import org.gradle.api.file.Directory;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.Property;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class Parameters {
    private final List<String> params = new ArrayList<>(60);
    private final Logger logger;
    private final Directory projectDirectory;

    public Parameters(Logger logger, Directory projectDirectory) {
        this.logger = logger;
        this.projectDirectory = projectDirectory;
    }

    public List<String> getParams() {
        return params;
    }

    public void add(String value) {
        logger.info("  {}", value);
        params.add(value);
    }

    public void addString(CommandLineParameter parameter, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        logger.info("  {} {}", parameter.getName(), value);
        params.add(parameter.getName());
        params.add(value);
    }

    public void addString(CommandLineParameter parameter, Property<String> prop) {
        String value = prop.getOrElse("");
        if (value.isEmpty()) {
            return;
        }

        logger.info("  {} {}", parameter.getName(), value);
        params.add(parameter.getName());
        params.add(value);
    }

    public void addBoolean(CommandLineParameter parameter, Property<Boolean> prop) {
        if (!prop.getOrElse(false)) {
            return;
        }

        logger.info("  {}", parameter.getName());
        params.add(parameter.getName());
    }

    public void addFile(CommandLineParameter parameter, Property<String> prop, boolean mustExist) {
        if (!prop.isPresent()) {
            return;
        }

        String value = prop.get();
        if (value.isEmpty()) {
            return;
        }

        File file = projectDirectory.file(value).getAsFile();
        if (mustExist && !file.exists()) {
            throw new GradleException("File or directory " + file.getAbsolutePath() + " does not exist");
        }

        logger.info("  {} {}", parameter.getName(), file.getAbsolutePath());
        params.add(parameter.getName());
        params.add(file.getAbsolutePath());
    }

    public void addFile(CommandLineParameter parameter, String value, boolean mustExist) {
        if (value == null || value.isEmpty()) {
            return;
        }

        File file = projectDirectory.file(value).getAsFile();
        if (mustExist && !file.exists()) {
            throw new GradleException("File or directory " + file.getAbsolutePath() + " does not exist");
        }

        logger.info("  {} {}", parameter.getName(), file.getAbsolutePath());
        params.add(parameter.getName());
        params.add(file.getAbsolutePath());
    }
}

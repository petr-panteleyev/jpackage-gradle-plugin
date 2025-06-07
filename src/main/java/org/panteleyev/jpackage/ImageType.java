/*
 Copyright © 2020-2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

public enum ImageType {
    DEFAULT(""),
    APP_IMAGE("app-image"),
    DMG("dmg"),
    PKG("pkg"),
    EXE("exe"),
    MSI("msi"),
    RPM("rpm"),
    DEB("deb");

    private final String value;

    ImageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

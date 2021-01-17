/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage;

public enum ImageType implements EnumParameter {
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

    @Override
    public String getValue() {
        return value;
    }
}

/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.jpackage

enum class ImageType(override val value: String) : EnumParameter {
    DEFAULT(""),
    APP_IMAGE("app-image"),
    DMG("dmg"),
    PKG("pkg"),
    EXE("exe"),
    MSI("msi"),
    RPM("rpm"),
    DEB("deb");
}
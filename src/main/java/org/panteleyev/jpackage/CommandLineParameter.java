/*
 Copyright © 2022-2023 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

import org.gradle.api.GradleException;

import static org.panteleyev.jpackage.JPackageTask.EXECUTABLE;

enum CommandLineParameter {
    NAME("--name"),
    DESTINATION("--dest"),
    VERBOSE("--verbose"),
    TYPE("--type"),
    APP_VERSION("--app-version"),
    COPYRIGHT("--copyright"),
    DESCRIPTION("--description"),
    RUNTIME_IMAGE("--runtime-image"),
    INPUT("--input"),
    INSTALL_DIR("--install-dir"),
    RESOURCE_DIR("--resource-dir"),
    VENDOR("--vendor"),
    MODULE("--module"),
    MAIN_CLASS("--main-class"),
    MAIN_JAR("--main-jar"),
    TEMP("--temp"),
    ICON("--icon"),
    LICENSE_FILE("--license-file"),
    APP_IMAGE("--app-image"),
    MODULE_PATH("--module-path"),
    ADD_MODULES("--add-modules"),
    JAVA_OPTIONS("--java-options"),
    ARGUMENTS("--arguments"),
    FILE_ASSOCIATIONS("--file-associations"),
    ADD_LAUNCHER("--add-launcher"),
    BIND_SERVICES("--bind-services", 14, 15),
    JLINK_OPTIONS("--jlink-options", 16),
    ABOUT_URL("--about-url", 17),
    APP_CONTENT("--app-content", 18),
    LAUNCHER_AS_SERVICE("--launcher-as-service", 19),
    // Mac
    MAC_PACKAGE_IDENTIFIER("--mac-package-identifier"),
    MAC_PACKAGE_NAME("--mac-package-name"),
    MAC_BUNDLE_SIGNING_PREFIX("--mac-bundle-signing-prefix", 14, 16),
    MAC_PACKAGE_SIGNING_PREFIX("--mac-package-signing-prefix", 17),
    MAC_APP_STORE("--mac-app-store", 17),
    MAC_ENTITLEMENTS("--mac-entitlements", 17),
    MAC_APP_CATEGORY("--mac-app-category", 17),
    MAC_SIGN("--mac-sign"),
    MAC_SIGNING_KEYCHAIN("--mac-signing-keychain"),
    MAC_SIGNING_KEY_USER_NAME("--mac-signing-key-user-name"),
    MAC_DMG_CONTENT("--mac-dmg-content", 18),
    // Windows
    WIN_CONSOLE("--win-console"),
    WIN_DIR_CHOOSER("--win-dir-chooser"),
    WIN_HELP_URL("--win-help-url", 17),
    WIN_MENU("--win-menu"),
    WIN_MENU_GROUP("--win-menu-group"),
    WIN_PER_USER_INSTALL("--win-per-user-install"),
    WIN_SHORTCUT("--win-shortcut"),
    WIN_SHORTCUT_PROMPT("--win-shortcut-prompt", 17),
    WIN_UPDATE_URL("--win-update-url", 17),
    WIN_UPGRADE_UUID("--win-upgrade-uuid"),
    // Linux
    LINUX_PACKAGE_NAME("--linux-package-name"),
    LINUX_DEB_MAINTAINER("--linux-deb-maintainer"),
    LINUX_MENU_GROUP("--linux-menu-group"),
    LINUX_PACKAGE_DEPS("--linux-package-deps"),
    LINUX_RPM_LICENSE_TYPE("--linux-rpm-license-type"),
    LINUX_APP_RELEASE("--linux-app-release"),
    LINUX_APP_CATEGORY("--linux-app-category"),
    LINUX_SHORTCUT("--linux-shortcut");

    private final String name;
    private final int minVersion;
    private final int maxVersion;

    CommandLineParameter(String name, int minVersion, int maxVersion) {
        this.name = name;
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
    }

    CommandLineParameter(String name, int minVersion) {
        this(name, minVersion, Integer.MAX_VALUE);
    }

    CommandLineParameter(String name) {
        this(name, 14, Integer.MAX_VALUE);
    }

    public String getName() {
        return name;
    }

    public void checkVersion(int version) throws GradleException {
        if (version == 0) {
            return;
        }

        if (version < minVersion || version > maxVersion) {
            throw new GradleException(
                    "Parameter \""
                            + name
                            + "\" requires "
                            + EXECUTABLE
                            + " versions: ["
                            + minVersion
                            + ".."
                            + (maxVersion == Integer.MAX_VALUE ? "*" : maxVersion)
                            + "]"
            );
        }
    }
}

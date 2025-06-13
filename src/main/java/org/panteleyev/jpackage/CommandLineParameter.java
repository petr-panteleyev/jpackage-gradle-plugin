/*
 Copyright © 2022-2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

enum CommandLineParameter {
    ABOUT_URL("--about-url"),
    ADD_LAUNCHER("--add-launcher"),
    ADD_MODULES("--add-modules"),
    APP_CONTENT("--app-content"),
    APP_IMAGE("--app-image"),
    APP_VERSION("--app-version"),
    ARGUMENTS("--arguments"),
    BIND_SERVICES("--bind-services"),
    COPYRIGHT("--copyright"),
    DESCRIPTION("--description"),
    DESTINATION("--dest"),
    FILE_ASSOCIATIONS("--file-associations"),
    ICON("--icon"),
    INPUT("--input"),
    INSTALL_DIR("--install-dir"),
    JAVA_OPTIONS("--java-options"),
    JLINK_OPTIONS("--jlink-options"),
    LAUNCHER_AS_SERVICE("--launcher-as-service"),
    LICENSE_FILE("--license-file"),
    MAIN_CLASS("--main-class"),
    MAIN_JAR("--main-jar"),
    MODULE("--module"),
    MODULE_PATH("--module-path"),
    NAME("--name"),
    RESOURCE_DIR("--resource-dir"),
    RUNTIME_IMAGE("--runtime-image"),
    TEMP("--temp"),
    TYPE("--type"),
    VENDOR("--vendor"),
    VERBOSE("--verbose"),
    // Mac
    MAC_APP_CATEGORY("--mac-app-category"),
    MAC_APP_STORE("--mac-app-store"),
    MAC_BUNDLE_SIGNING_PREFIX("--mac-bundle-signing-prefix"),
    MAC_DMG_CONTENT("--mac-dmg-content"),
    MAC_ENTITLEMENTS("--mac-entitlements"),
    MAC_PACKAGE_IDENTIFIER("--mac-package-identifier"),
    MAC_PACKAGE_NAME("--mac-package-name"),
    MAC_PACKAGE_SIGNING_PREFIX("--mac-package-signing-prefix"),
    MAC_SIGN("--mac-sign"),
    MAC_SIGNING_KEYCHAIN("--mac-signing-keychain"),
    MAC_SIGNING_KEY_USER_NAME("--mac-signing-key-user-name"),
    // Windows
    WIN_CONSOLE("--win-console"),
    WIN_DIR_CHOOSER("--win-dir-chooser"),
    WIN_HELP_URL("--win-help-url"),
    WIN_MENU("--win-menu"),
    WIN_MENU_GROUP("--win-menu-group"),
    WIN_PER_USER_INSTALL("--win-per-user-install"),
    WIN_SHORTCUT("--win-shortcut"),
    WIN_SHORTCUT_PROMPT("--win-shortcut-prompt"),
    WIN_UPDATE_URL("--win-update-url"),
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

    CommandLineParameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

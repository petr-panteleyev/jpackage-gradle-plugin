/*
 Copyright © 2021-2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.jvm.toolchain.JavaLauncher;
import org.gradle.jvm.toolchain.JavaToolchainService;
import org.gradle.jvm.toolchain.JavaToolchainSpec;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.panteleyev.jpackage.CommandLineParameter.ABOUT_URL;
import static org.panteleyev.jpackage.CommandLineParameter.ADD_LAUNCHER;
import static org.panteleyev.jpackage.CommandLineParameter.ADD_MODULES;
import static org.panteleyev.jpackage.CommandLineParameter.APP_CONTENT;
import static org.panteleyev.jpackage.CommandLineParameter.APP_IMAGE;
import static org.panteleyev.jpackage.CommandLineParameter.APP_VERSION;
import static org.panteleyev.jpackage.CommandLineParameter.ARGUMENTS;
import static org.panteleyev.jpackage.CommandLineParameter.BIND_SERVICES;
import static org.panteleyev.jpackage.CommandLineParameter.COPYRIGHT;
import static org.panteleyev.jpackage.CommandLineParameter.DESCRIPTION;
import static org.panteleyev.jpackage.CommandLineParameter.DESTINATION;
import static org.panteleyev.jpackage.CommandLineParameter.FILE_ASSOCIATIONS;
import static org.panteleyev.jpackage.CommandLineParameter.ICON;
import static org.panteleyev.jpackage.CommandLineParameter.INPUT;
import static org.panteleyev.jpackage.CommandLineParameter.INSTALL_DIR;
import static org.panteleyev.jpackage.CommandLineParameter.JAVA_OPTIONS;
import static org.panteleyev.jpackage.CommandLineParameter.JLINK_OPTIONS;
import static org.panteleyev.jpackage.CommandLineParameter.LAUNCHER_AS_SERVICE;
import static org.panteleyev.jpackage.CommandLineParameter.LICENSE_FILE;
import static org.panteleyev.jpackage.CommandLineParameter.LINUX_APP_CATEGORY;
import static org.panteleyev.jpackage.CommandLineParameter.LINUX_APP_RELEASE;
import static org.panteleyev.jpackage.CommandLineParameter.LINUX_DEB_MAINTAINER;
import static org.panteleyev.jpackage.CommandLineParameter.LINUX_MENU_GROUP;
import static org.panteleyev.jpackage.CommandLineParameter.LINUX_PACKAGE_DEPS;
import static org.panteleyev.jpackage.CommandLineParameter.LINUX_PACKAGE_NAME;
import static org.panteleyev.jpackage.CommandLineParameter.LINUX_RPM_LICENSE_TYPE;
import static org.panteleyev.jpackage.CommandLineParameter.LINUX_SHORTCUT;
import static org.panteleyev.jpackage.CommandLineParameter.MAC_APP_CATEGORY;
import static org.panteleyev.jpackage.CommandLineParameter.MAC_APP_STORE;
import static org.panteleyev.jpackage.CommandLineParameter.MAC_BUNDLE_SIGNING_PREFIX;
import static org.panteleyev.jpackage.CommandLineParameter.MAC_DMG_CONTENT;
import static org.panteleyev.jpackage.CommandLineParameter.MAC_ENTITLEMENTS;
import static org.panteleyev.jpackage.CommandLineParameter.MAC_PACKAGE_IDENTIFIER;
import static org.panteleyev.jpackage.CommandLineParameter.MAC_PACKAGE_NAME;
import static org.panteleyev.jpackage.CommandLineParameter.MAC_PACKAGE_SIGNING_PREFIX;
import static org.panteleyev.jpackage.CommandLineParameter.MAC_SIGN;
import static org.panteleyev.jpackage.CommandLineParameter.MAC_SIGNING_KEYCHAIN;
import static org.panteleyev.jpackage.CommandLineParameter.MAC_SIGNING_KEY_USER_NAME;
import static org.panteleyev.jpackage.CommandLineParameter.MAIN_CLASS;
import static org.panteleyev.jpackage.CommandLineParameter.MAIN_JAR;
import static org.panteleyev.jpackage.CommandLineParameter.MODULE;
import static org.panteleyev.jpackage.CommandLineParameter.MODULE_PATH;
import static org.panteleyev.jpackage.CommandLineParameter.NAME;
import static org.panteleyev.jpackage.CommandLineParameter.RESOURCE_DIR;
import static org.panteleyev.jpackage.CommandLineParameter.RUNTIME_IMAGE;
import static org.panteleyev.jpackage.CommandLineParameter.TEMP;
import static org.panteleyev.jpackage.CommandLineParameter.TYPE;
import static org.panteleyev.jpackage.CommandLineParameter.VENDOR;
import static org.panteleyev.jpackage.CommandLineParameter.VERBOSE;
import static org.panteleyev.jpackage.CommandLineParameter.WIN_CONSOLE;
import static org.panteleyev.jpackage.CommandLineParameter.WIN_DIR_CHOOSER;
import static org.panteleyev.jpackage.CommandLineParameter.WIN_HELP_URL;
import static org.panteleyev.jpackage.CommandLineParameter.WIN_MENU;
import static org.panteleyev.jpackage.CommandLineParameter.WIN_MENU_GROUP;
import static org.panteleyev.jpackage.CommandLineParameter.WIN_PER_USER_INSTALL;
import static org.panteleyev.jpackage.CommandLineParameter.WIN_SHORTCUT;
import static org.panteleyev.jpackage.CommandLineParameter.WIN_SHORTCUT_PROMPT;
import static org.panteleyev.jpackage.CommandLineParameter.WIN_UPDATE_URL;
import static org.panteleyev.jpackage.CommandLineParameter.WIN_UPGRADE_UUID;
import static org.panteleyev.jpackage.DirectoryUtil.isNestedDirectory;
import static org.panteleyev.jpackage.DirectoryUtil.removeDirectory;
import static org.panteleyev.jpackage.OsUtil.isLinux;
import static org.panteleyev.jpackage.OsUtil.isMac;
import static org.panteleyev.jpackage.OsUtil.isWindows;
import static org.panteleyev.jpackage.StringUtil.escape;

@SuppressWarnings({"SameParameterValue", "unused"})
public abstract class JPackageTask extends DefaultTask {
    static final String EXECUTABLE = "jpackage";

    private final String projectVersion = getProject().getVersion().toString();

    // Plugin internal options
    private final boolean dryRun;

    public JPackageTask() {
        dryRun = Boolean.getBoolean("jpackage.dryRun");

        try {
            JavaToolchainSpec toolchain = getProject().getExtensions()
                    .getByType(JavaPluginExtension.class).getToolchain();
            Provider<JavaLauncher> defaultLauncher = getJavaToolchainService().launcherFor(toolchain);
            getJavaLauncher().convention(defaultLauncher);
        } catch (Exception ex) {
            getLogger().trace("Failed to configure JavaLauncher");
        }
    }

    @Inject
    public abstract JavaToolchainService getJavaToolchainService();

    @Inject
    public abstract ProjectLayout getProjectLayout();

    @Nested
    @org.gradle.api.tasks.Optional
    public abstract Property<JavaLauncher> getJavaLauncher();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getAboutUrl();

    @InputFiles
    @org.gradle.api.tasks.Optional
    public abstract ConfigurableFileCollection getAppContent();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getAppDescription();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getAppImage();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getAppName();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getAppVersion();

    @InputFiles
    @org.gradle.api.tasks.Optional
    public abstract ConfigurableFileCollection getFileAssociations();

    @InputFile
    @org.gradle.api.tasks.Optional
    public abstract RegularFileProperty getIcon();

    @InputDirectory
    @org.gradle.api.tasks.Optional
    public abstract DirectoryProperty getRuntimeImage();

    @InputDirectory
    @org.gradle.api.tasks.Optional
    public abstract DirectoryProperty getInput();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getInstallDir();

    @OutputDirectory
    public abstract DirectoryProperty getDestination();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getModule();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getMainClass();

    @InputFile
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getMainJar();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getCopyright();

    @InputFiles
    @org.gradle.api.tasks.Optional
    public abstract ConfigurableFileCollection getModulePaths();

    @InputFile
    @org.gradle.api.tasks.Optional
    public abstract RegularFileProperty getLicenseFile();

    @InputDirectory
    @org.gradle.api.tasks.Optional
    public abstract DirectoryProperty getResourceDir();

    @InputFile
    @org.gradle.api.tasks.Optional
    public abstract DirectoryProperty getTemp();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract ListProperty<String> getJavaOptions();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract ListProperty<String> getArguments();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract ListProperty<Launcher> getLaunchers();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract ListProperty<String> getAddModules();

    @Input
    @org.gradle.api.tasks.Optional
    @Deprecated
    public abstract Property<Boolean> getBindServices();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract ListProperty<String> getJLinkOptions();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getLauncherAsService();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<ImageType> getType();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getVendor();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getVerbose();

    // Windows specific parameters

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getWinMenu();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getWinDirChooser();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getWinUpgradeUuid();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getWinMenuGroup();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getWinShortcut();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getWinPerUserInstall();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getWinConsole();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getWinHelpUrl();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getWinShortcutPrompt();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getWinUpdateUrl();

    // OS X specific parameters

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getMacPackageIdentifier();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getMacPackageName();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getMacPackageSigningPrefix();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getMacBundleSigningPrefix();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getMacSign();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getMacSigningKeychain();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getMacSigningKeyUserName();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getMacAppStore();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getMacAppCategory();

    @InputFile
    @org.gradle.api.tasks.Optional
    public abstract RegularFileProperty getMacEntitlements();

    @InputFiles
    @org.gradle.api.tasks.Optional
    public abstract ConfigurableFileCollection getMacDmgContent();

    // Linux specific parameters

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getLinuxPackageName();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getLinuxDebMaintainer();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getLinuxMenuGroup();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getLinuxRpmLicenseType();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getLinuxAppRelease();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getLinuxAppCategory();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getLinuxShortcut();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<Boolean> getLinuxPackageDeps();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract ListProperty<String> getAdditionalParameters();

    @Input
    @org.gradle.api.tasks.Optional
    public abstract MapProperty<String, String> getJpackageEnvironment();

    @TaskAction
    public void action() {
        if (dryRun) {
            getLogger().lifecycle("Executing jpackage plugin in dry run mode");
        }

        String jpackage = getJPackageFromToolchain()
                .orElseGet(() -> getJPackageFromJavaHome()
                        .orElseThrow(() -> new GradleException("Could not detect " + EXECUTABLE)));

        execute(jpackage);
    }

    private Optional<String> buildExecutablePath(String home) {
        String executable = home + File.separator + "bin" + File.separator + EXECUTABLE + (isWindows() ? ".exe" : "");
        if (new File(executable).exists()) {
            return Optional.of(executable);
        } else {
            getLogger().warn("File {} does not exist", executable);
            return Optional.empty();
        }
    }

    private Optional<String> getJPackageFromToolchain() {
        getLogger().info("Looking for {} in toolchain", EXECUTABLE);
        try {
            JavaLauncher launcherValue = getJavaLauncher().getOrNull();
            if (launcherValue == null) {
                throw new RuntimeException();
            } else {
                String home = launcherValue.getMetadata().getInstallationPath().getAsFile().getAbsolutePath();
                getLogger().info("toolchain: {}", home);
                return buildExecutablePath(home);
            }
        } catch (Exception ex) {
            getLogger().info("Toolchain is not configured");
            return Optional.empty();
        }
    }

    private Optional<String> getJPackageFromJavaHome() {
        getLogger().info("Getting {} from java.home", EXECUTABLE);
        String javaHome = System.getProperty("java.home");
        if (javaHome == null) {
            getLogger().error("java.home is not set");
            return Optional.empty();
        }
        return buildExecutablePath(javaHome);
    }

    private void buildParameters(Parameters parameters) {
        parameters.addString(ABOUT_URL, getAboutUrl());
        if (getLaunchers().isPresent()) {
            for (Launcher launcher : getLaunchers().get()) {
                File launcherFile = launcher.getFile();
                if (!launcherFile.exists()) {
                    throw new GradleException(
                            "Launcher file " + launcherFile.getAbsolutePath() + " does not exist");
                }
                parameters.addString(ADD_LAUNCHER, launcher.getName() + "=" + launcherFile.getAbsolutePath());
            }
        }
        if (getAddModules().isPresent()) {
            List<String> addModules = getAddModules().get();
            if (!addModules.isEmpty()) {
                parameters.addString(ADD_MODULES, String.join(",", addModules));
            }
        }

        getAppContent().forEach(file -> parameters.addFile(APP_CONTENT, file, true));
        getFileAssociations().forEach(file -> parameters.addFile(FILE_ASSOCIATIONS, file, true));

        parameters.addFile(APP_IMAGE, getAppImage(), true);
        parameters.addString(APP_VERSION, getAppVersion().getOrElse(projectVersion));
        if (getArguments().isPresent()) {
            for (Object arg : getArguments().get()) {
                parameters.addString(ARGUMENTS, escape(arg.toString()));
            }
        }
        parameters.addBoolean(BIND_SERVICES, getBindServices());
        parameters.addString(COPYRIGHT, getCopyright());
        parameters.addString(DESCRIPTION, getAppDescription());
        parameters.addFile(DESTINATION, getDestination(), false);

        parameters.addFile(ICON, getIcon(), true);
        parameters.addFile(INPUT, getInput(), true);
        parameters.addString(INSTALL_DIR, getInstallDir());
        if (getJavaOptions().isPresent()) {
            for (Object option : getJavaOptions().get()) {
                parameters.addString(JAVA_OPTIONS, escape(option.toString()));
            }
        }

        if (getJLinkOptions().isPresent()) {
            List<String> jLinkOptions = getJLinkOptions().get();
            if (!jLinkOptions.isEmpty()) {
                parameters.addString(JLINK_OPTIONS, String.join(" ", jLinkOptions));
            }
        }

        parameters.addBoolean(LAUNCHER_AS_SERVICE, getLauncherAsService());
        parameters.addFile(LICENSE_FILE, getLicenseFile(), true);
        parameters.addString(MAIN_CLASS, getMainClass());
        parameters.addString(MAIN_JAR, getMainJar());
        parameters.addString(MODULE, getModule());

        getModulePaths().forEach(file -> parameters.addFile(MODULE_PATH, file, true));

        parameters.addString(NAME, getAppName());
        parameters.addFile(RESOURCE_DIR, getResourceDir(), true);
        parameters.addFile(RUNTIME_IMAGE, getRuntimeImage(), true);
        parameters.addFile(TEMP, getTemp(), false);

        ImageType type = getType().getOrElse(ImageType.DEFAULT);
        if (type != ImageType.DEFAULT) {
            parameters.addString(TYPE, type.getValue());
        }

        parameters.addString(VENDOR, getVendor());
        parameters.addBoolean(VERBOSE, getVerbose());

        if (isMac()) {
            parameters.addString(MAC_APP_CATEGORY, getMacAppCategory());
            parameters.addBoolean(MAC_APP_STORE, getMacAppStore());
            parameters.addString(MAC_BUNDLE_SIGNING_PREFIX, getMacBundleSigningPrefix());
            getMacDmgContent().forEach(file -> parameters.addFile(MAC_DMG_CONTENT, file, true));
            parameters.addFile(MAC_ENTITLEMENTS, getMacEntitlements(), true);
            parameters.addString(MAC_PACKAGE_IDENTIFIER, getMacPackageIdentifier());
            parameters.addString(MAC_PACKAGE_NAME, getMacPackageName());
            parameters.addString(MAC_PACKAGE_SIGNING_PREFIX, getMacBundleSigningPrefix());
            parameters.addBoolean(MAC_SIGN, getMacSign());
            parameters.addString(MAC_SIGNING_KEY_USER_NAME, getMacSigningKeyUserName());
            parameters.addFile(MAC_SIGNING_KEYCHAIN, getMacSigningKeychain(), true);
        } else if (isWindows()) {
            parameters.addBoolean(WIN_CONSOLE, getWinConsole());
            parameters.addBoolean(WIN_DIR_CHOOSER, getWinDirChooser());
            parameters.addString(WIN_HELP_URL, getWinHelpUrl());
            parameters.addBoolean(WIN_MENU, getWinMenu());
            parameters.addString(WIN_MENU_GROUP, getWinMenuGroup());
            parameters.addBoolean(WIN_PER_USER_INSTALL, getWinPerUserInstall());
            parameters.addBoolean(WIN_SHORTCUT, getWinShortcut());
            parameters.addBoolean(WIN_SHORTCUT_PROMPT, getWinShortcutPrompt());
            parameters.addString(WIN_UPDATE_URL, getWinUpdateUrl());
            parameters.addString(WIN_UPGRADE_UUID, getWinUpgradeUuid());
        } else if (isLinux()) {
            parameters.addString(LINUX_APP_CATEGORY, getLinuxAppCategory());
            parameters.addString(LINUX_APP_RELEASE, getLinuxAppRelease());
            parameters.addString(LINUX_DEB_MAINTAINER, getLinuxDebMaintainer());
            parameters.addString(LINUX_MENU_GROUP, getLinuxMenuGroup());
            parameters.addBoolean(LINUX_PACKAGE_DEPS, getLinuxPackageDeps());
            parameters.addString(LINUX_PACKAGE_NAME, getLinuxPackageName());
            parameters.addString(LINUX_RPM_LICENSE_TYPE, getLinuxRpmLicenseType());
            parameters.addBoolean(LINUX_SHORTCUT, getLinuxShortcut());
        }

        // Additional options
        if (getAdditionalParameters().isPresent()) {
            for (Object option : getAdditionalParameters().get()) {
                parameters.add(option.toString());
            }
        }
    }

    private void execute(String cmd) {
        Parameters parameters = new Parameters(getLogger(), getProjectLayout().getProjectDirectory());
        parameters.add(cmd.contains(" ") ? "\"" + cmd + "\"" : cmd);
        buildParameters(parameters);

        if (dryRun) {
            return;
        }

        Path destinationPath = getDestination().getAsFile().get().toPath().toAbsolutePath();
        if (!isNestedDirectory(getProjectLayout().getBuildDirectory().getAsFile().get().toPath(), destinationPath)) {
            getLogger().error("Cannot remove destination folder, must belong to {}",
                    getProjectLayout().getBuildDirectory().get());
        } else {
            getLogger().warn("Trying to remove destination {}", destinationPath);
            removeDirectory(destinationPath);
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();

            if (getJpackageEnvironment().isPresent()) {
                Map<String, String> jPackageEnvironment = getJpackageEnvironment().get();
                if (!jPackageEnvironment.isEmpty()) {
                    getLogger().info(EXECUTABLE + " environment:");

                    Map<String, String> environment = processBuilder.environment();
                    for (Map.Entry<String, String> entry : jPackageEnvironment.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();

                        if (key == null || key.trim().isEmpty() || value == null) {
                            // Silently skip null or empty keys or null values
                            continue;
                        }

                        environment.put(key, value);
                        getLogger().info("  {} = {}", key, value);
                    }
                }
            }

            Process process = processBuilder
                    .redirectErrorStream(true)
                    .command(parameters.getParams())
                    .start();

            getLogger().info(EXECUTABLE + " output:");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    getLogger().info(line);
                }
            }

            int status = process.waitFor();
            if (status != 0) {
                throw new GradleException("Error while executing " + EXECUTABLE);
            }
        } catch (Exception ex) {
            throw new GradleException("Error while executing " + EXECUTABLE, ex);
        }
    }

    /**
     * Executes windows specific configuration block.
     *
     * @param block configuration block
     */
    public void windows(Runnable block) {
        if (isWindows()) {
            block.run();
        }
    }

    /**
     * Executes OS X specific configuration block.
     *
     * @param block configuration block
     */
    public void mac(Runnable block) {
        if (isMac()) {
            block.run();
        }
    }

    /**
     * Executes Linux specific configuration block.
     *
     * @param block configuration block
     */
    public void linux(Runnable block) {
        if (isLinux()) {
            block.run();
        }
    }

}

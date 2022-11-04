/*
 Copyright © 2021-2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.jpackage;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.jvm.toolchain.JavaLauncher;
import org.gradle.jvm.toolchain.JavaToolchainService;
import org.gradle.jvm.toolchain.JavaToolchainSpec;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
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
import static org.panteleyev.jpackage.OsUtil.isLinux;
import static org.panteleyev.jpackage.OsUtil.isMac;
import static org.panteleyev.jpackage.OsUtil.isWindows;
import static org.panteleyev.jpackage.StringUtil.escape;

@SuppressWarnings({"SameParameterValue", "unused"})
public class JPackageTask extends DefaultTask {
    static final String EXECUTABLE = "jpackage";

    private boolean verbose;
    private ImageType type = ImageType.DEFAULT;
    private String appName;
    private String appImage;
    private String appVersion = getProject().getVersion().toString();
    private String vendor;
    private String icon;
    private String runtimeImage;
    private String input;
    private String installDir;
    private String destination;
    private String module;
    private String mainClass;
    private String mainJar;
    private String copyright;
    private String appDescription;
    private List<String> modulePaths;
    private String licenseFile;
    private String resourceDir;
    private String temp;
    private List<String> javaOptions;
    private List<String> arguments;
    private List<String> fileAssociations;
    private List<Launcher> launchers;
    private List<String> addModules;
    private boolean bindServices;
    private List<String> jLinkOptions;
    private String aboutUrl;
    private boolean launcherAsService;
    private List<String> appContent;

    // Windows specific parameters
    private boolean winMenu;
    private boolean winDirChooser;
    private String winUpgradeUuid;
    private String winMenuGroup;
    private boolean winShortcut;
    private boolean winPerUserInstall;
    private boolean winConsole;
    private String winHelpUrl;
    private boolean winShortcutPrompt;
    private String winUpdateUrl;

    // OS X specific parameters
    private String macPackageIdentifier;
    private String macPackageName;
    private String macPackageSigningPrefix;
    private String macBundleSigningPrefix;
    private boolean macSign;
    private String macSigningKeychain;
    private String macSigningKeyUserName;
    private boolean macAppStore;
    private String macAppCategory;
    private String macEntitlements;
    private List<String> macDmgContent;

    // Linux specific parameters
    private String linuxPackageName;
    private String linuxDebMaintainer;
    private String linuxMenuGroup;
    private String linuxRpmLicenseType;
    private String linuxAppRelease;
    private String linuxAppCategory;
    private boolean linuxShortcut;
    private boolean linuxPackageDeps;

    // Additional parameters
    private List<String> additionalParameters = new ArrayList<>();

    // JPackage process environment variables
    private Map<String, String> jpackageEnvironment;

    // Plugin internal options
    private final boolean dryRun;

    public JPackageTask() {
        dryRun = Boolean.getBoolean("jpackage.dryRun");
    }

    @Input
    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    @Input
    public ImageType getType() {
        return type;
    }

    public void setType(ImageType type) {
        this.type = type;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getAppImage() {
        return appImage;
    }

    public void setAppImage(String appImage) {
        this.appImage = appImage;
    }

    @Input
    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getRuntimeImage() {
        return runtimeImage;
    }

    public void setRuntimeImage(String runtimeImage) {
        this.runtimeImage = runtimeImage;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getInstallDir() {
        return installDir;
    }

    public void setInstallDir(String installDir) {
        this.installDir = installDir;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getMainJar() {
        return mainJar;
    }

    public void setMainJar(String mainJar) {
        this.mainJar = mainJar;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public List<String> getModulePaths() {
        return modulePaths;
    }

    public void setModulePaths(List<String> modulePaths) {
        this.modulePaths = modulePaths;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getLicenseFile() {
        return licenseFile;
    }

    public void setLicenseFile(String licenseFile) {
        this.licenseFile = licenseFile;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getResourceDir() {
        return resourceDir;
    }

    public void setResourceDir(String resourceDir) {
        this.resourceDir = resourceDir;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public List<String> getJavaOptions() {
        return javaOptions;
    }

    public void setJavaOptions(List<String> javaOptions) {
        this.javaOptions = javaOptions;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public List<String> getFileAssociations() {
        return fileAssociations;
    }

    public void setFileAssociations(List<String> fileAssociations) {
        this.fileAssociations = fileAssociations;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public List<Launcher> getLaunchers() {
        return launchers;
    }

    public void setLaunchers(List<Launcher> launchers) {
        this.launchers = launchers;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public List<String> getAddModules() {
        return addModules;
    }

    public void setAddModules(List<String> addModules) {
        this.addModules = addModules;
    }

    @Input
    public boolean getBindServices() {
        return bindServices;
    }

    public void setBindServices(boolean bindServices) {
        this.bindServices = bindServices;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public List<String> getJLinkOptions() {
        return jLinkOptions;
    }

    public void setJLinkOptions(List<String> jLinkOptions) {
        this.jLinkOptions = jLinkOptions;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getAboutUrl() {
        return aboutUrl;
    }

    public void setAboutUrl(String aboutUrl) {
        this.aboutUrl = aboutUrl;
    }

    @Input
    public boolean getLauncherAsService() {
        return launcherAsService;
    }

    public void setLauncherAsService(boolean launcherAsService) {
        this.launcherAsService = launcherAsService;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public List<String> getAppContent() {
        return appContent;
    }

    public void setAppContent(List<String> appContent) {
        this.appContent = appContent;
    }

    @Input
    public boolean getWinMenu() {
        return winMenu;
    }

    public void setWinMenu(boolean winMenu) {
        this.winMenu = winMenu;
    }

    @Input
    public boolean getWinDirChooser() {
        return winDirChooser;
    }

    public void setWinDirChooser(boolean winDirChooser) {
        this.winDirChooser = winDirChooser;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getWinUpgradeUuid() {
        return winUpgradeUuid;
    }

    public void setWinUpgradeUuid(String winUpgradeUuid) {
        this.winUpgradeUuid = winUpgradeUuid;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getWinMenuGroup() {
        return winMenuGroup;
    }

    public void setWinMenuGroup(String winMenuGroup) {
        this.winMenuGroup = winMenuGroup;
    }

    @Input
    public boolean getWinShortcut() {
        return winShortcut;
    }

    public void setWinShortcut(boolean winShortcut) {
        this.winShortcut = winShortcut;
    }

    @Input
    public boolean getWinPerUserInstall() {
        return winPerUserInstall;
    }

    public void setWinPerUserInstall(boolean winPerUserInstall) {
        this.winPerUserInstall = winPerUserInstall;
    }

    @Input
    public boolean getWinConsole() {
        return winConsole;
    }

    public void setWinConsole(boolean winConsole) {
        this.winConsole = winConsole;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getWinHelpUrl() {
        return winHelpUrl;
    }

    public void setWinHelpUrl(String winHelpUrl) {
        this.winHelpUrl = winHelpUrl;
    }

    @Input
    public boolean getWinShortcutPrompt() {
        return winShortcutPrompt;
    }

    public void setWinShortcutPrompt(boolean winShortcutPrompt) {
        this.winShortcutPrompt = winShortcutPrompt;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getWinUpdateUrl() {
        return winUpdateUrl;
    }

    public void setWinUpdateUrl(String winUpdateUrl) {
        this.winUpdateUrl = winUpdateUrl;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getMacPackageIdentifier() {
        return macPackageIdentifier;
    }

    public void setMacPackageIdentifier(String macPackageIdentifier) {
        this.macPackageIdentifier = macPackageIdentifier;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getMacPackageName() {
        return macPackageName;
    }

    public void setMacPackageName(String macPackageName) {
        this.macPackageName = macPackageName;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getMacPackageSigningPrefix() {
        return macPackageSigningPrefix;
    }

    public void setMacPackageSigningPrefix(String macPackageSigningPrefix) {
        this.macPackageSigningPrefix = macPackageSigningPrefix;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getMacBundleSigningPrefix() {
        return macBundleSigningPrefix;
    }

    public void setMacBundleSigningPrefix(String macBundleSigningPrefix) {
        this.macBundleSigningPrefix = macBundleSigningPrefix;
    }

    @Input
    public boolean getMacSign() {
        return macSign;
    }

    public void setMacSign(boolean macSign) {
        this.macSign = macSign;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getMacSigningKeychain() {
        return macSigningKeychain;
    }

    public void setMacSigningKeychain(String macSigningKeychain) {
        this.macSigningKeychain = macSigningKeychain;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getMacSigningKeyUserName() {
        return macSigningKeyUserName;
    }

    public void setMacSigningKeyUserName(String macSigningKeyUserName) {
        this.macSigningKeyUserName = macSigningKeyUserName;
    }

    @Input
    public boolean getMacAppStore() {
        return macAppStore;
    }

    public void setMacAppStore(boolean macAppStore) {
        this.macAppStore = macAppStore;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getMacAppCategory() {
        return macAppCategory;
    }

    public void setMacAppCategory(String macAppCategory) {
        this.macAppCategory = macAppCategory;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getMacEntitlements() {
        return macEntitlements;
    }

    public void setMacEntitlements(String macEntitlements) {
        this.macEntitlements = macEntitlements;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public List<String> getMacDmgContent() {
        return macDmgContent;
    }

    public void setMacDmgContent(List<String> macDmgContent) {
        this.macDmgContent = macDmgContent;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getLinuxPackageName() {
        return linuxPackageName;
    }

    public void setLinuxPackageName(String linuxPackageName) {
        this.linuxPackageName = linuxPackageName;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getLinuxDebMaintainer() {
        return linuxDebMaintainer;
    }

    public void setLinuxDebMaintainer(String linuxDebMaintainer) {
        this.linuxDebMaintainer = linuxDebMaintainer;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getLinuxMenuGroup() {
        return linuxMenuGroup;
    }

    public void setLinuxMenuGroup(String linuxMenuGroup) {
        this.linuxMenuGroup = linuxMenuGroup;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getLinuxRpmLicenseType() {
        return linuxRpmLicenseType;
    }

    public void setLinuxRpmLicenseType(String linuxRpmLicenseType) {
        this.linuxRpmLicenseType = linuxRpmLicenseType;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getLinuxAppRelease() {
        return linuxAppRelease;
    }

    public void setLinuxAppRelease(String linuxAppRelease) {
        this.linuxAppRelease = linuxAppRelease;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public String getLinuxAppCategory() {
        return linuxAppCategory;
    }

    public void setLinuxAppCategory(String linuxAppCategory) {
        this.linuxAppCategory = linuxAppCategory;
    }

    @Input
    public boolean getLinuxShortcut() {
        return linuxShortcut;
    }

    public void setLinuxShortcut(boolean linuxShortcut) {
        this.linuxShortcut = linuxShortcut;
    }

    @Input
    public boolean getLinuxPackageDeps() {
        return linuxPackageDeps;
    }

    public void setLinuxPackageDeps(boolean linuxPackageDeps) {
        this.linuxPackageDeps = linuxPackageDeps;
    }

    @Input
    public List<String> getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(List<String> additionalParameters) {
        this.additionalParameters = additionalParameters;
    }

    @Input
    @org.gradle.api.tasks.Optional
    public Map<String, String> getJpackageEnvironment() {
        return jpackageEnvironment;
    }

    public void setJpackageEnvironment(Map<String, String> jpackageEnvironment) {
        this.jpackageEnvironment = jpackageEnvironment;
    }

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

    @SuppressWarnings("UnstableApiUsage")
    private Optional<String> getJPackageFromToolchain() {
        getLogger().info("Looking for {} in toolchain", EXECUTABLE);
        try {
            JavaToolchainSpec toolchain = getProject().getExtensions().getByType(JavaPluginExtension.class).getToolchain();
            JavaToolchainService service = getProject().getExtensions().getByType(JavaToolchainService.class);
            Provider<JavaLauncher> defaultLauncher = service.launcherFor(toolchain);
            String home = defaultLauncher.get().getMetadata().getInstallationPath().getAsFile().getAbsolutePath();

            getLogger().info("toolchain: " + home);
            return buildExecutablePath(home);
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

    private void buildParameters(Collection<String> parameters, int version) {
        addParameter(parameters, ABOUT_URL, aboutUrl, version);
        if (launchers != null) {
            for (Launcher launcher : launchers) {
                File launcherFile = getProject().file(launcher.getFilePath());
                if (!launcherFile.exists()) {
                    throw new GradleException("Launcher file " + launcherFile.getAbsolutePath() + " does not exist");
                }
                addParameter(parameters, ADD_LAUNCHER,
                        launcher.getName() + "=" + launcherFile.getAbsolutePath(), version);
            }
        }
        if (addModules != null && !addModules.isEmpty()) {
            addParameter(parameters, ADD_MODULES, String.join(",", addModules), version);
        }
        if (appContent != null) {
            for (Object appContentElement : appContent) {
                addFileParameter(parameters, APP_CONTENT, appContentElement.toString(), true, version);
            }
        }
        addFileParameter(parameters, APP_IMAGE, appImage, version);
        addParameter(parameters, APP_VERSION, appVersion, version);
        if (arguments != null) {
            for (Object arg : arguments) {
                addParameter(parameters, ARGUMENTS, escape(arg.toString()), version);
            }
        }
        addParameter(parameters, BIND_SERVICES, bindServices, version);
        addParameter(parameters, COPYRIGHT, copyright, version);
        addParameter(parameters, DESCRIPTION, appDescription, version);
        addFileParameter(parameters, DESTINATION, destination, false, version);
        if (fileAssociations != null) {
            for (Object association : fileAssociations) {
                addFileParameter(parameters, FILE_ASSOCIATIONS, association.toString(), true, version);
            }
        }
        addFileParameter(parameters, ICON, icon, true, version);
        addFileParameter(parameters, INPUT, input, true, version);
        addParameter(parameters, INSTALL_DIR, installDir, version);
        if (javaOptions != null) {
            for (Object option : javaOptions) {
                addParameter(parameters, JAVA_OPTIONS, escape(option.toString()), version);
            }
        }
        if (jLinkOptions != null && !jLinkOptions.isEmpty()) {
            addParameter(parameters, JLINK_OPTIONS, String.join(" ", jLinkOptions), version);
        }
        addParameter(parameters, LAUNCHER_AS_SERVICE, launcherAsService, version);
        addFileParameter(parameters, LICENSE_FILE, licenseFile, true, version);
        addParameter(parameters, MAIN_CLASS, mainClass, version);
        addParameter(parameters, MAIN_JAR, mainJar, version);
        addParameter(parameters, MODULE, module, version);
        if (modulePaths != null) {
            for (Object path : modulePaths) {
                addFileParameter(parameters, MODULE_PATH, path.toString(), version);
            }
        }
        addParameter(parameters, NAME, appName, version);
        addFileParameter(parameters, RESOURCE_DIR, resourceDir, true, version);
        addFileParameter(parameters, RUNTIME_IMAGE, runtimeImage, true, version);
        addFileParameter(parameters, TEMP, temp, false, version);
        if (type != ImageType.DEFAULT) {
            addParameter(parameters, TYPE, type, version);
        }
        addParameter(parameters, VENDOR, vendor, version);
        addParameter(parameters, VERBOSE, verbose, version);

        if (isMac()) {
            addParameter(parameters, MAC_APP_CATEGORY, macAppCategory, version);
            addParameter(parameters, MAC_APP_STORE, macAppStore, version);
            addParameter(parameters, MAC_BUNDLE_SIGNING_PREFIX, macBundleSigningPrefix, version);
            if (macDmgContent != null) {
                for (Object dmgContent : macDmgContent) {
                    addFileParameter(parameters, MAC_DMG_CONTENT, dmgContent.toString(), true, version);
                }
            }
            addFileParameter(parameters, MAC_ENTITLEMENTS, macEntitlements, true, version);
            addParameter(parameters, MAC_PACKAGE_IDENTIFIER, macPackageIdentifier, version);
            addParameter(parameters, MAC_PACKAGE_NAME, macPackageName, version);
            addParameter(parameters, MAC_PACKAGE_SIGNING_PREFIX, macPackageSigningPrefix, version);
            addParameter(parameters, MAC_SIGN, macSign, version);
            addParameter(parameters, MAC_SIGNING_KEY_USER_NAME, macSigningKeyUserName, version);
            addFileParameter(parameters, MAC_SIGNING_KEYCHAIN, macSigningKeychain, true, version);
        } else if (isWindows()) {
            addParameter(parameters, WIN_CONSOLE, winConsole, version);
            addParameter(parameters, WIN_DIR_CHOOSER, winDirChooser, version);
            addParameter(parameters, WIN_HELP_URL, winHelpUrl, version);
            addParameter(parameters, WIN_MENU, winMenu, version);
            addParameter(parameters, WIN_MENU_GROUP, winMenuGroup, version);
            addParameter(parameters, WIN_PER_USER_INSTALL, winPerUserInstall, version);
            addParameter(parameters, WIN_SHORTCUT, winShortcut, version);
            addParameter(parameters, WIN_SHORTCUT_PROMPT, winShortcutPrompt, version);
            addParameter(parameters, WIN_UPDATE_URL, winUpdateUrl, version);
            addParameter(parameters, WIN_UPGRADE_UUID, winUpgradeUuid, version);
        } else if (isLinux()) {
            addParameter(parameters, LINUX_APP_CATEGORY, linuxAppCategory, version);
            addParameter(parameters, LINUX_APP_RELEASE, linuxAppRelease, version);
            addParameter(parameters, LINUX_DEB_MAINTAINER, linuxDebMaintainer, version);
            addParameter(parameters, LINUX_MENU_GROUP, linuxMenuGroup, version);
            addParameter(parameters, LINUX_PACKAGE_DEPS, linuxPackageDeps, version);
            addParameter(parameters, LINUX_PACKAGE_NAME, linuxPackageName, version);
            addParameter(parameters, LINUX_RPM_LICENSE_TYPE, linuxRpmLicenseType, version);
            addParameter(parameters, LINUX_SHORTCUT, linuxShortcut, version);
        }

        // Additional options
        for (Object option : additionalParameters) {
            addAdditionalParameter(parameters, option.toString());
        }
    }

    private void execute(String cmd) {
        int version = getMajorVersion(cmd);
        if (version == 0) {
            throw new GradleException("Could not determine " + EXECUTABLE + " version");
        } else {
            getLogger().info("Using: {}, major version: {}", cmd, version);
        }

        List<String> parameters = new ArrayList<>();
        parameters.add(cmd.contains(" ") ? "\"" + cmd + "\"" : cmd);
        buildParameters(parameters, version);

        if (dryRun) {
            return;
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();

            if (jpackageEnvironment != null && !jpackageEnvironment.isEmpty()) {
                getLogger().info(EXECUTABLE + " environment:");

                Map<String, String> environment = processBuilder.environment();
                for (Map.Entry<String, String> entry : jpackageEnvironment.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (key == null || key.trim().isEmpty() || value == null) {
                        // Silently skip null or empty keys or null values
                        continue;
                    }

                    environment.put(key, value);
                    getLogger().info("  " + key + " = " + value);
                }
            }

            Process process = processBuilder
                    .redirectErrorStream(true)
                    .command(parameters)
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


    private int getMajorVersion(String cmd) {
        List<String> parameters = new ArrayList<>();
        parameters.add(cmd.contains(" ") ? "\"" + cmd + "\"" : cmd);
        parameters.add("--version");

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            Process process = processBuilder
                    .redirectErrorStream(true)
                    .command(parameters)
                    .start();

            int result = 0;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                if (line != null) {
                    String[] parts = line.split("\\.");
                    result = Integer.parseInt(parts[0]);
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

    private void addParameter(Collection<String> params, String name, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        getLogger().info("  " + name + " " + value);
        params.add(name);
        params.add(value);
    }

    private void addParameter(Collection<String> params, CommandLineParameter parameter, String value, int version) {
        if (value == null || value.isEmpty()) {
            return;
        }

        parameter.checkVersion(version);

        getLogger().info("  " + parameter.getName() + " " + value);
        params.add(parameter.getName());
        params.add(value);
    }

    private void addFileParameter(Collection<String> params, CommandLineParameter parameter, String value, int version) {
        addFileParameter(params, parameter, value, true, version);
    }

    private void addFileParameter(Collection<String> params, CommandLineParameter parameter, String value, boolean mustExist, int version) {
        if (value == null || value.isEmpty()) {
            return;
        }

        parameter.checkVersion(version);

        File file = getProject().file(value);
        if (mustExist && !file.exists()) {
            throw new GradleException("File or directory " + file.getAbsolutePath() + " does not exist");
        }

        addParameter(params, parameter.getName(), file.getAbsolutePath());
    }

    private void addParameter(Collection<String> params, String name, boolean value) {
        if (!value) {
            return;
        }

        getLogger().info("  " + name);
        params.add(name);
    }

    private void addParameter(Collection<String> params, CommandLineParameter parameter, boolean value, int version) {
        if (!value) {
            return;
        }

        parameter.checkVersion(version);

        getLogger().info("  " + parameter.getName());
        params.add(parameter.getName());
    }

    private void addParameter(Collection<String> params, CommandLineParameter parameter, EnumParameter value, int version) {
        if (value == null) {
            return;
        }

        parameter.checkVersion(version);

        addParameter(params, parameter.getName(), value.getValue());
    }

    private void addAdditionalParameter(Collection<String> params, String parameter) {
        if (parameter == null || parameter.isEmpty()) {
            return;
        }
        getLogger().info("  " + parameter);
        params.add(parameter);
    }
}

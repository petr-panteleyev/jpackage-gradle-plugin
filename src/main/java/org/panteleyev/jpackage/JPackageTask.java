/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
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
import java.util.Optional;
import static org.panteleyev.jpackage.OsUtil.isLinux;
import static org.panteleyev.jpackage.OsUtil.isMac;
import static org.panteleyev.jpackage.OsUtil.isWindows;
import static org.panteleyev.jpackage.StringUtil.escape;

@SuppressWarnings({"SameParameterValue", "unused"})
public class JPackageTask extends DefaultTask {
    public static final String EXECUTABLE = "jpackage";

    private boolean verbose = false;
    private ImageType type = ImageType.DEFAULT;
    private String appName = "";
    private String appImage = "";
    private String appVersion = getProject().getVersion().toString();
    private String vendor = "";
    private String icon = "";
    private String runtimeImage = "";
    private String input = "";
    private String installDir = "";
    private String destination = "";
    private String module = "";
    private String mainClass = "";
    private String mainJar = "";
    private String copyright = "";
    private String appDescription = "";
    private List<String> modulePaths = new ArrayList<>();
    private String licenseFile = "";
    private String resourceDir = "";
    private String temp = "";
    private List<String> javaOptions = new ArrayList<>();
    private List<String> arguments = new ArrayList<>();
    private List<String> fileAssociations = new ArrayList<>();
    private List<Launcher> launchers = new ArrayList<>();
    private List<String> addModules = new ArrayList<>();

    // Windows specific parameters
    private boolean winMenu = false;
    private boolean winDirChooser = false;
    private String winUpgradeUuid = "";
    private String winMenuGroup = "";
    private boolean winShortcut = false;
    private boolean winPerUserInstall = false;
    private boolean winConsole = false;

    // OS X specific parameters
    private String macPackageIdentifier = "";
    private String macPackageName = "";
    private String macPackageSigningPrefix = "";
    private boolean macSign = false;
    private String macSigningKeychain = "";
    private String macSigningKeyUserName = "";

    // Linux specific parameters
    private String linuxPackageName = "";
    private String linuxDebMaintainer = "";
    private String linuxMenuGroup = "";
    private String linuxRpmLicenseType = "";
    private String linuxAppRelease = "";
    private String linuxAppCategory = "";
    private boolean linuxShortcut = false;

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
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Input
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
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @Input
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Input
    public String getRuntimeImage() {
        return runtimeImage;
    }

    public void setRuntimeImage(String runtimeImage) {
        this.runtimeImage = runtimeImage;
    }

    @Input
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Input
    public String getInstallDir() {
        return installDir;
    }

    public void setInstallDir(String installDir) {
        this.installDir = installDir;
    }

    @Input
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Input
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Input
    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    @Input
    public String getMainJar() {
        return mainJar;
    }

    public void setMainJar(String mainJar) {
        this.mainJar = mainJar;
    }

    @Input
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @Input
    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    @Input
    public List<String> getModulePaths() {
        return modulePaths;
    }

    public void setModulePaths(List<String> modulePaths) {
        this.modulePaths = modulePaths;
    }

    @Input
    public String getLicenseFile() {
        return licenseFile;
    }

    public void setLicenseFile(String licenseFile) {
        this.licenseFile = licenseFile;
    }

    @Input
    public String getResourceDir() {
        return resourceDir;
    }

    public void setResourceDir(String resourceDir) {
        this.resourceDir = resourceDir;
    }

    @Input
    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    @Input
    public List<String> getJavaOptions() {
        return javaOptions;
    }

    public void setJavaOptions(List<String> javaOptions) {
        this.javaOptions = javaOptions;
    }

    @Input
    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    @Input
    public List<String> getFileAssociations() {
        return fileAssociations;
    }

    public void setFileAssociations(List<String> fileAssociations) {
        this.fileAssociations = fileAssociations;
    }

    @Input
    public List<Launcher> getLaunchers() {
        return launchers;
    }

    public void setLaunchers(List<Launcher> launchers) {
        this.launchers = launchers;
    }

    @Input
    public List<String> getAddModules() {
        return addModules;
    }

    public void setAddModules(List<String> addModules) {
        this.addModules = addModules;
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
    public String getWinUpgradeUuid() {
        return winUpgradeUuid;
    }

    public void setWinUpgradeUuid(String winUpgradeUuid) {
        this.winUpgradeUuid = winUpgradeUuid;
    }

    @Input
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
    public String getMacPackageIdentifier() {
        return macPackageIdentifier;
    }

    public void setMacPackageIdentifier(String macPackageIdentifier) {
        this.macPackageIdentifier = macPackageIdentifier;
    }

    @Input
    public String getMacPackageName() {
        return macPackageName;
    }

    public void setMacPackageName(String macPackageName) {
        this.macPackageName = macPackageName;
    }

    @Input
    public String getMacPackageSigningPrefix() {
        return macPackageSigningPrefix;
    }

    public void setMacPackageSigningPrefix(String macPackageSigningPrefix) {
        this.macPackageSigningPrefix = macPackageSigningPrefix;
    }

    @Input
    public boolean getMacSign() {
        return macSign;
    }

    public void setMacSign(boolean macSign) {
        this.macSign = macSign;
    }

    @Input
    public String getMacSigningKeychain() {
        return macSigningKeychain;
    }

    public void setMacSigningKeychain(String macSigningKeychain) {
        this.macSigningKeychain = macSigningKeychain;
    }

    @Input
    public String getMacSigningKeyUserName() {
        return macSigningKeyUserName;
    }

    public void setMacSigningKeyUserName(String macSigningKeyUserName) {
        this.macSigningKeyUserName = macSigningKeyUserName;
    }

    @Input
    public String getLinuxPackageName() {
        return linuxPackageName;
    }

    public void setLinuxPackageName(String linuxPackageName) {
        this.linuxPackageName = linuxPackageName;
    }

    @Input
    public String getLinuxDebMaintainer() {
        return linuxDebMaintainer;
    }

    public void setLinuxDebMaintainer(String linuxDebMaintainer) {
        this.linuxDebMaintainer = linuxDebMaintainer;
    }

    @Input
    public String getLinuxMenuGroup() {
        return linuxMenuGroup;
    }

    public void setLinuxMenuGroup(String linuxMenuGroup) {
        this.linuxMenuGroup = linuxMenuGroup;
    }

    @Input
    public String getLinuxRpmLicenseType() {
        return linuxRpmLicenseType;
    }

    public void setLinuxRpmLicenseType(String linuxRpmLicenseType) {
        this.linuxRpmLicenseType = linuxRpmLicenseType;
    }

    @Input
    public String getLinuxAppRelease() {
        return linuxAppRelease;
    }

    public void setLinuxAppRelease(String linuxAppRelease) {
        this.linuxAppRelease = linuxAppRelease;
    }

    @Input
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

    @TaskAction
    public void action() {
        String jpackage = getJPackageFromToolchain()
            .orElseGet(() -> getJPackageFromJavaHome()
                .orElseThrow(() -> new GradleException("Could not detect " + EXECUTABLE)));

        getLogger().info("Using: " + jpackage);
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

    private void buildParameters(Collection<String> parameters) {
        if (type != ImageType.DEFAULT) {
            addParameter(parameters, "--type", type);
        }

        addParameter(parameters, "--verbose", verbose);
        addParameter(parameters, "--name", appName);
        addParameter(parameters, "--app-version", appVersion);
        addParameter(parameters, "--copyright", copyright);
        addParameter(parameters, "--description", appDescription);
        addParameter(parameters, "--install-dir", installDir);
        addParameter(parameters, "--vendor", vendor);
        addParameter(parameters, "--module", module);
        addParameter(parameters, "--main-class", mainClass);
        addParameter(parameters, "--main-jar", mainJar);
        if (addModules != null && !addModules.isEmpty()) {
            addParameter(parameters, "--add-modules", String.join(",", addModules));
        }
        for (String arg : arguments) {
            addParameter(parameters, "--arguments", escape(arg));
        }
        for (String option : javaOptions) {
            addParameter(parameters, "--java-options", escape(option));
        }

        // File parameters
        addFileParameter(parameters, "--app-image", appImage);
        addFileParameter(parameters, "--dest", destination, false);
        for (String association : fileAssociations) {
            addFileParameter(parameters, "--file-associations", association);
        }
        addFileParameter(parameters, "--icon", icon);
        addFileParameter(parameters, "--input", input);
        addFileParameter(parameters, "--license-file", licenseFile);
        for (String path : modulePaths) {
            addFileParameter(parameters, "--module-path", path);
        }
        addFileParameter(parameters, "--resource-dir", resourceDir);
        addFileParameter(parameters, "--runtime-image", runtimeImage);
        addFileParameter(parameters, "--temp", temp);
        for (Launcher launcher : launchers) {
            addParameter(parameters, "--add-launcher",
                launcher.getName() + "=" + launcher.getAbsolutePath());
        }

        if (isMac()) {
            addParameter(parameters, "--mac-package-identifier", macPackageIdentifier);
            addParameter(parameters, "--mac-package-name", macPackageName);
            addParameter(parameters, "--mac-package-signing-prefix", macPackageSigningPrefix);
            addParameter(parameters, "--mac-sign", macSign);
            addParameter(parameters, "--mac-signing-key-user-name", macSigningKeyUserName);
            addFileParameter(parameters, "--mac-signing-keychain", macSigningKeychain);
        } else if (isWindows()) {
            addParameter(parameters, "--win-menu", winMenu);
            addParameter(parameters, "--win-dir-chooser", winDirChooser);
            addParameter(parameters, "--win-upgrade-uuid", winUpgradeUuid);
            addParameter(parameters, "--win-menu-group", winMenuGroup);
            addParameter(parameters, "--win-shortcut", winShortcut);
            addParameter(parameters, "--win-per-user-install", winPerUserInstall);
            addParameter(parameters, "--win-console", winConsole);
        } else if (isLinux()) {
            addParameter(parameters, "--linux-package-name", linuxPackageName);
            addParameter(parameters, "--linux-deb-maintainer", linuxDebMaintainer);
            addParameter(parameters, "--linux-menu-group", linuxMenuGroup);
            addParameter(parameters, "--linux-rpm-license-type", linuxRpmLicenseType);
            addParameter(parameters, "--linux-app-release", linuxAppRelease);
            addParameter(parameters, "--linux-app-category", linuxAppCategory);
            addParameter(parameters, "--linux-shortcut", linuxShortcut);
        }
    }

    private void execute(String cmd) {
        List<String> parameters = new ArrayList<>();
        parameters.add(cmd.contains(" ") ? "\"" + cmd + "\"" : cmd);
        buildParameters(parameters);

        try {
            Process process = new ProcessBuilder()
                .redirectErrorStream(true)
                .command(parameters)
                .start();

            getLogger().info("jpackage output:");

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

    private void addParameter(Collection<String> params, String name, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        getLogger().info("  " + name + " " + value);
        params.add(name);
        params.add(value);
    }

    private void addParameter(Collection<String> params, String name, File value) {
        if (value == null) {
            return;
        }

        String path = value.getAbsolutePath();
        getLogger().info("  " + name + " " + path);
        params.add(name);
        params.add(path);
    }

    private void addFileParameter(Collection<String> params, String name, String value) {
        addFileParameter(params, name, value, true);
    }

    private void addFileParameter(Collection<String> params, String name, String value, boolean mustExist) {
        if (value == null || value.isEmpty()) {
            return;
        }

        File file = getProject().file(value);
        if (mustExist && !file.exists()) {
            throw new GradleException("File or directory " + file.getAbsolutePath() + " does not exist");
        }

        addParameter(params, name, file.getAbsolutePath());
    }

    private void addParameter(Collection<String> params, String name, boolean value) {
        if (!value) {
            return;
        }

        getLogger().info("  " + name);
        params.add(name);
    }

    private void addParameter(Collection<String> params, String name, EnumParameter value) {
        if (value == null) {
            return;
        }

        addParameter(params, name, value.getValue());
    }
}

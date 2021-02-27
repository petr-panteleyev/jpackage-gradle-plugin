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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.panteleyev.jpackage.OsUtil.isLinux;
import static org.panteleyev.jpackage.OsUtil.isMac;
import static org.panteleyev.jpackage.OsUtil.isWindows;
import static org.panteleyev.jpackage.StringUtil.escape;

public class JPackageTask extends DefaultTask {
    public static final String EXECUTABLE = "jpackage";

    @Input
    private boolean verbose = false;

    @Input
    private ImageType type = ImageType.DEFAULT;

    @Input
    private String appName = "";

    @Input
    private String appVersion = getProject().getVersion().toString();

    @Input
    private String vendor = "";

    @Input
    private String icon = "";

    @Input
    private String runtimeImage = "";

    @Input
    private String input = "";

    @Input
    private String installDir = "";

    @Input
    private String destination = "";

    @Input
    private String module = "";

    @Input
    private String mainClass = "";

    @Input
    private String mainJar = "";

    @Input
    private String copyright = "";

    @Input
    private String appDescription = "";

    @Input
    private String modulePath = "";

    @Input
    private String licenseFile = "";

    @Input
    private String resourceDir = "";

    @Input
    private String temp = "";

    @Input
    private List<String> javaOptions = new ArrayList<>();

    @Input
    private List<String> arguments = new ArrayList<>();

    // Windows specific parameters
    @Input
    private boolean winMenu = false;

    @Input
    private boolean winDirChooser = false;

    @Input
    private String winUpgradeUuid = "";

    @Input
    private String winMenuGroup = "";

    @Input
    private boolean winShortcut = false;

    @Input
    private boolean winPerUserInstall = false;

    // OS X specific parameters
    @Input
    private String macPackageIdentifier = "";

    @Input
    private String macPackageName = "";

    @Input
    private String macPackageSigningPrefix = "";

    @Input
    private boolean macSign = false;

    @Input
    private String macSigningKeychain = "";

    @Input
    private String macSigningKeyUserName = "";

    // Linux specific parameters

    @Input
    private String linuxPackageName = "";

    @Input
    private String linuxDebMaintainer = "";

    @Input
    private String linuxMenuGroup = "";

    @Input
    private String linuxRpmLicenseType = "";

    @Input
    private String linuxAppRelease = "";

    @Input
    private String linuxAppCategory = "";

    @Input
    private boolean linuxShortcut = false;

    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public ImageType getType() {
        return type;
    }

    public void setType(ImageType type) {
        this.type = type;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRuntimeImage() {
        return runtimeImage;
    }

    public void setRuntimeImage(String runtimeImage) {
        this.runtimeImage = runtimeImage;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getInstallDir() {
        return installDir;
    }

    public void setInstallDir(String installDir) {
        this.installDir = installDir;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getMainJar() {
        return mainJar;
    }

    public void setMainJar(String mainJar) {
        this.mainJar = mainJar;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    public String getLicenseFile() {
        return licenseFile;
    }

    public void setLicenseFile(String licenseFile) {
        this.licenseFile = licenseFile;
    }

    public String getResourceDir() {
        return resourceDir;
    }

    public void setResourceDir(String resourceDir) {
        this.resourceDir = resourceDir;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public List<String> getJavaOptions() {
        return javaOptions;
    }

    public void setJavaOptions(List<String> javaOptions) {
        this.javaOptions = javaOptions;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public boolean getWinMenu() {
        return winMenu;
    }

    public void setWinMenu(boolean winMenu) {
        this.winMenu = winMenu;
    }

    public boolean getWinDirChooser() {
        return winDirChooser;
    }

    public void setWinDirChooser(boolean winDirChooser) {
        this.winDirChooser = winDirChooser;
    }

    public String getWinUpgradeUuid() {
        return winUpgradeUuid;
    }

    public void setWinUpgradeUuid(String winUpgradeUuid) {
        this.winUpgradeUuid = winUpgradeUuid;
    }

    public String getWinMenuGroup() {
        return winMenuGroup;
    }

    public void setWinMenuGroup(String winMenuGroup) {
        this.winMenuGroup = winMenuGroup;
    }

    public boolean getWinShortcut() {
        return winShortcut;
    }

    public void setWinShortcut(boolean winShortcut) {
        this.winShortcut = winShortcut;
    }

    public boolean getWinPerUserInstall() {
        return winPerUserInstall;
    }

    public void setWinPerUserInstall(boolean winPerUserInstall) {
        this.winPerUserInstall = winPerUserInstall;
    }

    public String getMacPackageIdentifier() {
        return macPackageIdentifier;
    }

    public void setMacPackageIdentifier(String macPackageIdentifier) {
        this.macPackageIdentifier = macPackageIdentifier;
    }

    public String getMacPackageName() {
        return macPackageName;
    }

    public void setMacPackageName(String macPackageName) {
        this.macPackageName = macPackageName;
    }

    public String getMacPackageSigningPrefix() {
        return macPackageSigningPrefix;
    }

    public void setMacPackageSigningPrefix(String macPackageSigningPrefix) {
        this.macPackageSigningPrefix = macPackageSigningPrefix;
    }

    public boolean getMacSign() {
        return macSign;
    }

    public void setMacSign(boolean macSign) {
        this.macSign = macSign;
    }

    public String getMacSigningKeychain() {
        return macSigningKeychain;
    }

    public void setMacSigningKeychain(String macSigningKeychain) {
        this.macSigningKeychain = macSigningKeychain;
    }

    public String getMacSigningKeyUserName() {
        return macSigningKeyUserName;
    }

    public void setMacSigningKeyUserName(String macSigningKeyUserName) {
        this.macSigningKeyUserName = macSigningKeyUserName;
    }

    public String getLinuxPackageName() {
        return linuxPackageName;
    }

    public void setLinuxPackageName(String linuxPackageName) {
        this.linuxPackageName = linuxPackageName;
    }

    public String getLinuxDebMaintainer() {
        return linuxDebMaintainer;
    }

    public void setLinuxDebMaintainer(String linuxDebMaintainer) {
        this.linuxDebMaintainer = linuxDebMaintainer;
    }

    public String getLinuxMenuGroup() {
        return linuxMenuGroup;
    }

    public void setLinuxMenuGroup(String linuxMenuGroup) {
        this.linuxMenuGroup = linuxMenuGroup;
    }

    public String getLinuxRpmLicenseType() {
        return linuxRpmLicenseType;
    }

    public void setLinuxRpmLicenseType(String linuxRpmLicenseType) {
        this.linuxRpmLicenseType = linuxRpmLicenseType;
    }

    public String getLinuxAppRelease() {
        return linuxAppRelease;
    }

    public void setLinuxAppRelease(String linuxAppRelease) {
        this.linuxAppRelease = linuxAppRelease;
    }

    public String getLinuxAppCategory() {
        return linuxAppCategory;
    }

    public void setLinuxAppCategory(String linuxAppCategory) {
        this.linuxAppCategory = linuxAppCategory;
    }

    public boolean getLinuxShortcut() {
        return linuxShortcut;
    }

    public void setLinuxShortcut(boolean linuxShortcut) {
        this.linuxShortcut = linuxShortcut;
    }

    @TaskAction
    public void action() {
        String jpackage = getJPackageFromToolchain();
        if (jpackage == null) {
            jpackage = getJPackageFromJavaHome();
        }
        getLogger().info("Using: " + jpackage);

        try {
            execute(jpackage);
        } catch (Exception ex) {
            throw new GradleException("Error while executing jpackage", ex);
        }
    }

    private String buildExecutablePath(String home) {
        String executable = home + File.separator + "bin" + File.separator + EXECUTABLE;
        return isWindows() ? executable + ".exe" : executable;
    }

    private String getJPackageFromToolchain() {
        getLogger().info("Looking for $EXECUTABLE in toolchain");
        try {
            JavaToolchainSpec toolchain = getProject().getExtensions().getByType(JavaPluginExtension.class).getToolchain();
            JavaToolchainService service = getProject().getExtensions().getByType(JavaToolchainService.class);
            Provider<JavaLauncher> defaultLauncher = service.launcherFor(toolchain);
            String home = defaultLauncher.get().getMetadata().getInstallationPath().getAsFile().getAbsolutePath();
            String executable = buildExecutablePath(home);
            if (new File(executable).exists()) {
                return executable;
            } else {
                getLogger().warn("File {} does not exist", executable);
                return null;
            }
        } catch (Exception ex) {
            getLogger().warn("Toolchain is not configured");
            return null;
        }
    }

    private String getJPackageFromJavaHome() {
        getLogger().info("Getting {} from java.home", EXECUTABLE);
        String javaHome = System.getProperty("java.home");
        if (javaHome == null) {
            throw new GradleException("java.home is not set");
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
        addParameter(parameters, "--dest", destination);
        addParameter(parameters, "--copyright", copyright);
        addParameter(parameters, "--description", appDescription);
        addParameter(parameters, "--runtime-image", runtimeImage);
        addParameter(parameters, "--input", input);
        addParameter(parameters, "--install-dir", installDir);
        addParameter(parameters, "--vendor", vendor);
        addParameter(parameters, "--module", module);
        addParameter(parameters, "--main-class", mainClass);
        addParameter(parameters, "--main-jar", mainJar);
        addParameter(parameters, "--module-path", modulePath);
        addParameter(parameters, "--icon", icon);
        addParameter(parameters, "--license-file", licenseFile);
        addParameter(parameters, "--resource-dir", resourceDir);
        addParameter(parameters, "--temp", temp);

        for (String option : javaOptions) {
            addParameter(parameters, "--java-options", escape(option));
        }

        for (String arg : arguments) {
            addParameter(parameters, "--arguments", escape(arg));
        }

        if (isMac()) {
            addParameter(parameters, "--mac-package-identifier", macPackageIdentifier);
            addParameter(parameters, "--mac-package-name", macPackageName);
            addParameter(parameters, "--mac-package-signing-prefix", macPackageSigningPrefix);
            addParameter(parameters, "--mac-sign", macSign);
            addParameter(parameters, "--mac-signing-keychain", macSigningKeychain);
            addParameter(parameters, "--mac-signing-key-user-name", macSigningKeyUserName);
        } else if (isWindows()) {
            addParameter(parameters, "--win-menu", winMenu);
            addParameter(parameters, "--win-dir-chooser", winDirChooser);
            addParameter(parameters, "--win-upgrade-uuid", winUpgradeUuid);
            addParameter(parameters, "--win-menu-group", winMenuGroup);
            addParameter(parameters, "--win-shortcut", winShortcut);
            addParameter(parameters, "--win-per-user-install", winPerUserInstall);
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

    private void execute(String cmd) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> parameters = new ArrayList<>();
        parameters.add(cmd.contains(" ") ? "\"" + cmd + "\"" : cmd);
        buildParameters(parameters);
        processBuilder.command(parameters);
        Process process = processBuilder.start();
        getLogger().info("jpackage output:");
        logCmdOutput(process.getInputStream());
        logCmdOutput(process.getErrorStream());
        int status = process.waitFor();
        if (status != 0) {
            throw new GradleException("Error while executing $EXECUTABLE");
        }
    }

    private void logCmdOutput(InputStream stream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                getLogger().info(line);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
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

// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.jpackage;

import java.io.Serial;
import java.io.Serializable;

public class JLink implements Serializable {
    @Serial
    private static final long serialVersionUID = 202606280045L;

    private boolean bindServices;
    private boolean noHeaderFiles;
    private boolean noManPages;
    private boolean stripDebug;
    private boolean stripNativeCommands;
    private boolean generateCdsArchive;

    public JLink() {
    }

    JLink(boolean bindServices, boolean noHeaderFiles, boolean noManPages, boolean stripDebug,
            boolean stripNativeCommands, boolean generateCdsArchive)
    {
        this.bindServices = bindServices;
        this.noHeaderFiles = noHeaderFiles;
        this.noManPages = noManPages;
        this.stripDebug = stripDebug;
        this.stripNativeCommands = stripNativeCommands;
        this.generateCdsArchive = generateCdsArchive;
    }

    public void setBindServices(boolean bindServices) {
        this.bindServices = bindServices;
    }

    public boolean getBindServices() {
        return bindServices;
    }

    public void setNoHeaderFiles(boolean noHeaderFiles) {
        this.noHeaderFiles = noHeaderFiles;
    }

    public boolean getNoHeaderFiles() {
        return noHeaderFiles;
    }

    public void setNoManPages(boolean noManPages) {
        this.noManPages = noManPages;
    }

    public boolean getNoManPages() {
        return noManPages;
    }

    public void setStripDebug(boolean stripDebug) {
        this.stripDebug = stripDebug;
    }

    public boolean getStripDebug() {
        return stripDebug;
    }

    public void setStripNativeCommands(boolean stripNativeCommands) {
        this.stripNativeCommands = stripNativeCommands;
    }

    public boolean getStripNativeCommands() {
        return stripNativeCommands;
    }

    public void setGenerateCdsArchive(boolean generateCdsArchive) {
        this.generateCdsArchive = generateCdsArchive;
    }

    public boolean getGenerateCdsArchive() {
        return generateCdsArchive;
    }

    String build() {
        return (
                (bindServices ? "--bind-services " : "")
                        + (noHeaderFiles ? "--no-header-files " : "")
                        + (noManPages ? "--no-man-pages " : "")
                        + (stripDebug ? "--strip-debug " : "")
                        + (stripNativeCommands ? "--strip-native-commands " : "")
                        + (generateCdsArchive ? "--generate-cds-archive " : "")
        ).trim();
    }
}

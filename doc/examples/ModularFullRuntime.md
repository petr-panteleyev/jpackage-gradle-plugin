# Modular Application with Full Runtime

```kotlin
tasks.register("copyDependencies", Copy::class) {
    from(configurations.runtimeClasspath).into("${layout.buildDirectory.get()}/jmods")
}

tasks.register("copyJar", Copy::class) {
    from(tasks.jar).into("${layout.buildDirectory.get()}/jmods")
}

tasks.jpackage {
    dependsOn("build", "copyDependencies", "copyJar")

    appName = "Application Name"
    appVersion = project.version.toString()
    vendor = "app.org"
    copyright = "Copyright (c) 2020 Vendor"
    runtimeImage = System.getProperty("java.home")
    module = "org.app.module/org.app.MainClass"
    modulePaths = listOf("${layout.buildDirectory.get()}/jmods")
    destination = "${layout.buildDirectory.get()}/dist"
    javaOptions = listOf("-Dfile.encoding=UTF-8")

    mac {
        icon = "${layout.projectDirectory}/icons/icons.icns"
    }

    windows {
        icon = "${layout.projectDirectory}/icons/icons.ico"
        winMenu = true
        winDirChooser = true
    }
}
```

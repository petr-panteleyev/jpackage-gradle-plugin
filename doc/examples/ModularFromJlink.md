# Modular Application with Custom Runtime

This example shows how to reuse custom image built with [jlink-gradle-plugin](https://github.com/petr-panteleyev/jlink-gradle-plugin):

```kotlin
tasks.register("copyDependencies", Copy::class) {
    from(configurations.runtimeClasspath).into(layout.buildDirectory.dir("jmods"))
}

tasks.register("copyJar", Copy::class) {
    from(tasks.jar).into(layout.buildDirectory.dir("jmods"))
}

tasks.jlink {
    dependsOn("build", "copyDependencies", "copyJar")

    modulePaths.setFrom(tasks.named("copyJar"))
    addModules = listOf("ALL-MODULE-PATH")

    output.set(layout.buildDirectory.dir("jlink"))
}

tasks.jpackage {
    appName = "Application Name"
    appVersion = project.version.toString()
    vendor = "app.org"
    copyright = "Copyright (c) 2020 Vendor"
    runtimeImage = tasks.jlink.get().output
    module = "org.app.module/org.app.MainClass"
    destination = layout.buildDirectory.dir("dist")
    javaOptions = listOf("-Dfile.encoding=UTF-8")

    mac {
        icon = layout.projectDirectory.file("icons/icons.icns")
    }

    windows {
        icon = layout.projectDirectory.file("icons/icons.ico")
        winMenu = true
        winDirChooser = true
    }
}
```

# Modular Application with Full Runtime

```kotlin
tasks.register("copyDependencies", Copy::class) {
    from(configurations.runtimeClasspath).into(layout.buildDirectory.dir("jmods"))
}

tasks.register("copyJar", Copy::class) {
    from(tasks.jar).into(layout.buildDirectory.dir("jmods"))
}

tasks.jpackage {
    dependsOn("build", "copyDependencies", "copyJar")

    appName = "Application Name"
    appVersion = project.version.toString()
    vendor = "app.org"
    copyright = "Copyright (c) 2020 Vendor"
    runtimeImage = File(System.getProperty("java.home"))
    module = "org.app.module/org.app.MainClass"
    modulePaths.setFrom(tasks.named("copyJar"))
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

# Non-Modular Application

```kotlin
tasks.register("copyDependencies", Copy::class) {
    from(configurations.runtimeClasspath).into(layout.buildDirectory.dir("jars"))
}

tasks.register("copyJar", Copy::class) {
    from(tasks.jar).into(layout.buildDirectory.dir("jars"))
}

tasks.jpackage {
    dependsOn("build", "copyDependencies", "copyJar")

    input = layout.buildDirectory.dir("jars")
    destination = layout.buildDirectory.dir("dist")

    appName = "Non-Modular Application"
    vendor = "app.org"

    mainJar = tasks.jar.get().archiveFileName.get()
    mainClass = "org.app.MainClass"

    javaOptions = listOf("-Dfile.encoding=UTF-8")

    windows {
        winConsole = true
    }
}

```

# Non-Modular Application

```kotlin
task("copyDependencies", Copy::class) {
    from(configurations.runtimeClasspath).into("${layout.buildDirectory.get()}//jars")
}

task("copyJar", Copy::class) {
    from(tasks.jar).into("${layout.buildDirectory.get()}//jars")
}

tasks.jpackage {
    dependsOn("build", "copyDependencies", "copyJar")

    input = "${layout.buildDirectory.get()}/jars"
    destination = "${layout.buildDirectory.get()}/dist"

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

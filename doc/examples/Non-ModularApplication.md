# Non-Modular Application

```kotlin
task("copyDependencies", Copy::class) {
    from(configurations.runtimeClasspath).into("$buildDir/jars")
}

task("copyJar", Copy::class) {
    from(tasks.jar).into("$buildDir/jars")
}

tasks.jpackage {
    dependsOn("build", "copyDependencies", "copyJar")

    input  = "$buildDir/jars"
    destination = "$buildDir/dist"

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

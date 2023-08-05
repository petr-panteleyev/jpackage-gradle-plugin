# Modular Application with Full Runtime

```groovy
tasks.register('copyDependencies', Copy) {
  from(configurations.runtimeClasspath).into("$buildDir/jmods")
}

tasks.register('copyJar', Copy) {
  from(tasks.jar).into("$buildDir/jmods")
}

tasks.jpackage {
  dependsOn("build", "copyDependencies", "copyJar")

  appName = "Application Name"
  appVersion = project.version.toString()
  vendor = "app.org"
  copyright = "Copyright (c) 2020 Vendor"
  runtimeImage = System.getProperty("java.home")
  module = 'modulename'
  def buildDirectory = new File("$buildDir/jmods")
  modulePaths = buildDirectory.listFiles().findAll { it.isFile() }.toList()
  destination = "$buildDir/dist"
  javaOptions = ["-Dfile.encoding=UTF-8"]

  mac {
    icon = "icons/icon.icns"
  }

  linux {
    icon = "icons/icon.png"
  }

  windows {
    icon = "icons/icon.ico"
    winMenu = true
    winDirChooser = true
  }
}
```

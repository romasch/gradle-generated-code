# gradle-generated-code
A small gradle plugin that adds a Java source set for generated code.

Basically, it does the following things:
* Defines a new source set in src/gen/java
* Add dependencies from the main java sources to the generated code
* Hook into the clean task to automatically delete all generated sources.
* If the idea module is present, it also marks src/gen/java as a generated source folder. 

On its own the plugin is not really useful, but it can be used by other Java code generator plugins:

```Groovy
class MyPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.apply (plugin: 'ch.romasch.gradle.generated-code')
        project.generate.dependsOn project.tasks.withType(MyPluginTask)
    }
}
```

## How to use it

Currently the plugin is not published or registered anywhere. To use it locally:
* git clone the repository
* ./gradlew build install
* In your own plugin, add the following to your build.gradle:
```Gradle
repositories {
    mavenLocal()
    // ...
}

dependencies {
    compile 'ch.romasch.gradle:generated-code:1.0-SNAPSHOT'
    // ...
}
```

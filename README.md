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

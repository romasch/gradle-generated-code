package ch.romasch.gradle.codegen

import org.gradle.api.Plugin
import org.gradle.api.Project

class GeneratedCodePlugin implements Plugin<Project> {

    public static final String CODE_GENERATION_TASK_GROUP = 'Code generation'
    private static final String GENERATE_TASK = "generate"
    private static final String CLEAN_TASK = "cleanGenerate"
    private static final String TARGET_DIRECTORY = "src/gen/java"

    void apply(Project project) {

        project.apply(plugin: 'java')

         project.sourceSets {
            gen {
                java.srcDir (project.file(TARGET_DIRECTORY))
            }
            main {
                compileClasspath += gen.output
                runtimeClasspath += gen.output
            }
        }

        project.task(GENERATE_TASK,
                group: CODE_GENERATION_TASK_GROUP,
                description: 'Execute tasks that generate Java source code.') {
            doLast {
                // Do nothing - this task exists such that other code generators can add their dependency to this.
            }
        }

        project.task(CLEAN_TASK,
                group: CODE_GENERATION_TASK_GROUP,
                description: 'Delete generated java source code generated from config files.') {
            doLast {
                project.file(TARGET_DIRECTORY).deleteDir()
            }
        }


        project.compileGenJava.dependsOn project.generate
        project.clean.dependsOn project.cleanGenerate

        project.afterEvaluate {
            if (project.plugins.findPlugin('idea') != null) {
                project.idea.module {
                    generatedSourceDirs += project.file(TARGET_DIRECTORY)
                }
            }
        }
    }
}

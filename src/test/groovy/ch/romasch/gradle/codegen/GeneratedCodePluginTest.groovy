package ch.romasch.gradle.codegen

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

class GeneratedCodePluginTest {

    private static final String PLUGIN_NAME = 'ch.romasch.gradle.generated-code'

    private Project project

    @Before
    void setup() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply(PLUGIN_NAME)
    }

    @Test
    void generateTasksExist() {
        assertTrue('generate task should exist', project.tasks.generate != null)
        assertTrue('generateClean task should exist', project.tasks.cleanGenerate != null)
    }

    @Test
    void sourceSetExists() {
        assertTrue('source set should exist', project.sourceSets.gen != null)
    }

    @Test
    void dependenciesAreSet() {
        assertTrue('compileGenJava should have dependency', project.compileGenJava.getDependsOn().contains(project.generate))
        assertTrue('clean should have dependency', project.clean.getDependsOn().contains(project.cleanGenerate))
    }

    @Test
    void multiApplyPossible() {
        project.pluginManager.apply(PLUGIN_NAME)
    }

}

package ch.romasch.gradle.codegen

import groovy.io.FileType
import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder


import static org.gradle.testkit.runner.TaskOutcome.SUCCESS
import static org.junit.Assert.assertTrue

class FunctionalPluginTest {

    @Rule public final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile


    @Before
    void setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            plugins {
                id 'ch.romasch.gradle.generated-code'
            }
            """
    }

    @Test
    void generateTaskSmokeTest() {
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments(':generate')
            .withPluginClasspath()
            .build()
        assertTrue("Task outcome should be successful", result.task(":generate").outcome == SUCCESS)
    }

    @Test
    void sourceSetDependenciesCorrect() {

        def genFolder = testProjectDir.newFolder('src', 'gen', 'java')
        def generated = new File(genFolder,'Generated.java')
        generated << "public class Generated {}"


        def mainFolder = testProjectDir.newFolder('src', 'main', 'java')
        def real = new File(mainFolder, 'Real.java')
        real << "public class Real { Generated generated; }"

        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments(':build')
                .withPluginClasspath()
                .withDebug(true)
                .build()

        assertTrue("Task outcome should be successful", result.task(":build").outcome == SUCCESS)
    }

    @Test
    void cleanGenerateTest() {
        def genFolder = testProjectDir.newFolder('src', 'gen', 'java', 'another', 'folder')
        def mainFolder = testProjectDir.newFolder('src', 'main', 'java', 'another', 'folder')


        def generated = new File(genFolder,'generated.java')
        generated.write('Generated, to be deleted')

        def real = new File(mainFolder, 'real.java')
        real.write('Should be kept')

        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments(':clean')
                .withPluginClasspath()
                .build()

        def files = []

        testProjectDir.root.eachFileRecurse (FileType.FILES) {
            files << it
        }

        assertTrue('Should contain real file', files.contains(real))
        assertTrue('Should not contain generated file', !files.contains(generated))
        assertTrue('Should contain build.gradle', files.contains(new File(testProjectDir.root, 'build.gradle')))
        assertTrue("Task outcome should be successful", result.task(":cleanGenerate").outcome == SUCCESS)
    }

}

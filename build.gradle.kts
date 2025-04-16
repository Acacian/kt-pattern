plugins {
    kotlin("jvm") version "1.9.22"
    `maven-publish`
}

group = "com.ktpattern"
version = "0.1.0"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(kotlin("test"))
    }

    tasks.test {
        useJUnitPlatform()
    }

    if (name in listOf("dsl-core", "dsl-runtime", "dsl-dsl")) {
        apply(plugin = "maven-publish")

        afterEvaluate {
            publishing {
                publications {
                    create<MavenPublication>("mavenJava") {
                        from(components["java"])
                        groupId = project.group.toString()
                        artifactId = project.name
                        version = project.version.toString()
                    }
                }
            }
        }
    }
}

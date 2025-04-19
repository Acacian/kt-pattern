plugins {
    kotlin("jvm") version "1.9.22"
}

group = "com.ktpattern"
version = "0.1.2"

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

    // 퍼블리시 대상 모듈만 maven-publish 적용
    if (name in listOf("dsl-core", "dsl-runtime", "dsl-dsl")) {
        apply(plugin = "maven-publish")

        afterEvaluate {
            extensions.configure<PublishingExtension>("publishing") {
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

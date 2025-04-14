plugins {
    kotlin("jvm")
    jacoco
}

dependencies {
    implementation(project(":dsl-core"))
    testImplementation(kotlin("test"))
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

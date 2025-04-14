plugins {
    kotlin("jvm")
    jacoco
}

dependencies {
    implementation(project(":dsl-core"))
    implementation(project(":dsl-runtime"))
    testImplementation(kotlin("test"))
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

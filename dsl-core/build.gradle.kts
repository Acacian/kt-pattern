plugins {
    kotlin("jvm")
    jacoco
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

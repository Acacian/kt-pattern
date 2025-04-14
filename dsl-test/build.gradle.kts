plugins {
    kotlin("jvm")
    jacoco
}

dependencies {
    implementation(project(":dsl-core"))
    implementation(project(":dsl-runtime"))
    implementation(project(":dsl-dsl"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped", "standardOut", "standardError")
        showStandardStreams = true
    }
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
        files(
            "${project(":dsl-dsl").buildDir}/classes/kotlin/main",
            "${project(":dsl-core").buildDir}/classes/kotlin/main",
            "${project(":dsl-runtime").buildDir}/classes/kotlin/main"
        )
    )

    sourceDirectories.setFrom(
        files(
            "${project(":dsl-dsl").projectDir}/src/main/kotlin",
            "${project(":dsl-core").projectDir}/src/main/kotlin",
            "${project(":dsl-runtime").projectDir}/src/main/kotlin"
        )
    )

    executionData.setFrom(
        fileTree(buildDir).include("jacoco/test.exec")
    )
}
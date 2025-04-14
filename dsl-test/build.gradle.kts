plugins {
    kotlin("jvm")
    jacoco
}

dependencies {
    // DSL 모듈 의존성 추가
    implementation(project(":dsl-core"))
    implementation(project(":dsl-runtime"))
    implementation(project(":dsl-dsl"))

    // JUnit5 + Kotlin test
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

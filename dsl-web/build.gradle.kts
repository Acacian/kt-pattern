plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "1.9.0"
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

dependencies {
    // DSL core modules
    implementation(project(":dsl-core"))
    implementation(project(":dsl-runtime"))
    implementation(project(":dsl-dsl"))

    // Spring Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Kotlin 기본
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
}

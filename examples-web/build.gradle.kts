plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "1.9.22"
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    application
}

application {
    mainClass.set("com.ktpattern.patternmatch.ExampleWebKt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(project(":dsl-core"))
    implementation(project(":dsl-dsl"))
    runtimeOnly(project(":dsl-runtime"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

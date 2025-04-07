plugins {
    kotlin("jvm") version "1.9.22"
}

group = "com.ktpattern"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

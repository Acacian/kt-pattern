plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":dsl-core"))
    testImplementation(kotlin("test"))
}

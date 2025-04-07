plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":dsl-core"))
    implementation(project(":dsl-runtime"))
    
    testImplementation(kotlin("test"))
}
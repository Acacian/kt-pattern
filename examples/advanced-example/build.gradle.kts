plugins {
    kotlin("jvm")
    application
}

application {
    mainClass.set("com.ktpattern.patternmatch.ExampleAdvancedKt")
}

dependencies {
    implementation(project(":dsl-core"))
    implementation(project(":dsl-dsl"))
    implementation(project(":dsl-runtime"))
}

plugins {
    kotlin("jvm")
    application
}

application {
    mainClass.set("com.ktpattern.patternmatch.ExampleBasicKt")
}

dependencies {
    implementation(project(":dsl-core"))
    implementation(project(":dsl-dsl"))
    runtimeOnly(project(":dsl-runtime"))
}

plugins {
    kotlin("jvm")
    application
}

application {
    mainClass.set("com.ktpattern.patternmatch.ExampleAppKt")
}

dependencies {
    implementation(project(":dsl-core"))
    implementation(project(":dsl-dsl"))
    runtimeOnly(project(":dsl-runtime")) 
}

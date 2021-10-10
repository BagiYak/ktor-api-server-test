val ktor_version: String by project
val kotlin_version: String by project
val kotlin_serialization: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationServerKt")
}

repositories {
    mavenCentral()
}

dependencies {

    // server
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")

    // client
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")

    // serialization
    implementation("io.ktor:ktor-serialization:$kotlin_serialization")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")

    // auth
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")


    // log
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // test
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}

val MAIN_CLASS = "haxidenti.jmonna.MainKt"
val PROJECT_NAME = "jmonna"
val AUTHOR = "HaxiDenti"
val VERSION = "1.0.0"
val CORO_VER = "1.9.0-RC"

plugins {
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("application")
    `maven-publish`
}

group = AUTHOR
version = VERSION

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("io.javalin:javalin:6.2.0")
    implementation("com.google.code.gson:gson:2.11.0")
}

application {
    mainClass.set(MAIN_CLASS)
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = AUTHOR
            artifactId = PROJECT_NAME
            version = VERSION
            
            from(components["java"])
        }
    }
}
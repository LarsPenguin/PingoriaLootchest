plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "com.pingoria"
version = "1.0.0"
description = "PingoriaLootchest - A loot chest plugin for PaperMC"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.build {
    dependsOn(tasks.reobfJar)
}

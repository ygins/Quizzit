import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.6.21"
}

group = "com.github.yona168"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.junit.jupiter:junit-jupiter:5.8.2")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.github.yona168.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Msi)
            packageName = "Quizzit"
            packageVersion = "1.0.0"
        }
    }
}

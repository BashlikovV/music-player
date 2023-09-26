plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                with(compose) {
                    implementation(runtime)
                    implementation(foundation)
                    implementation(material3)
                    implementation(material)
                    implementation(materialIconsExtended)
                    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                    implementation(components.resources)
                }

                with(Dependencies.Io.InsertKoin) {
                    implementation(koinCore)
                    implementation(koinCompose)
                }
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.AndroidX.Core.core)
                implementation(compose.uiTooling)
                implementation(compose.preview)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "by.bashlikovvv.music_player"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}
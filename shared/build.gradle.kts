plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("kotlin-parcelize")
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
                    implementation(compose.uiTooling)
                    implementation(compose.preview)
                }

                with(Dependencies.Io.InsertKoin) {
                    implementation(koinCore)
                    implementation(koinCompose)
                }

                with(Dependencies.Com.Arkivanov.MviKotlin) {
                    implementation(mviKotlin)
                    implementation(mviKotlinMain)
                    implementation(mviKotlinExtensionsCoroutines)
                }

                with(Dependencies.Com.Arkivanov.Decompose) {
                    implementation(decompose)
                    implementation(extensionsCompose)
                }
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.AndroidX.Core.core)
                implementation(Dependencies.Io.InsertKoin.koinAndroid)
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
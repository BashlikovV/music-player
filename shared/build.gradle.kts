plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("kotlin-parcelize")
    id("app.cash.sqldelight")
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
                    implementation(Dependencies.AndroidX.ConstraintLayout.constraintLayoutCompose)
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

                with(Dependencies.App.Cash.SQLDelight) {
                    implementation(runtime)
                    implementation(coroutinesExtensions)
                }

                implementation(Dependencies.Com.Arkivanov.Essenty.lifecycle)
                implementation(Dependencies.Org.JetBrains.KotlinX.serializationJson)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.AndroidX.Core.core)
                implementation(Dependencies.Io.InsertKoin.koinAndroid)
                implementation(Dependencies.App.Cash.SQLDelight.androidDriver)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

sqldelight {
    databases {
        create("TracksDatabase") {
            packageName.set("by.bashlikovvv.music_player.tracksDatabase")
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
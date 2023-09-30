plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "by.bashlikovvv.music_player.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "by.bashlikovvv.music_player.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(Dependencies.AndroidX.Compose.Ui.ui)
    implementation(Dependencies.AndroidX.Compose.Ui.uiTooling)
    implementation(Dependencies.AndroidX.Compose.Ui.uiToolingPreview)
    implementation(Dependencies.AndroidX.Compose.Foundation.foundation)
    implementation(Dependencies.AndroidX.Compose.Material.material)
    implementation(Dependencies.AndroidX.Activity.activityCompose)

    implementation(Dependencies.AndroidX.Core.core)
    implementation(Dependencies.Io.InsertKoin.koinAndroid)

    implementation(Dependencies.Com.Arkivanov.Decompose.decompose)
    implementation(Dependencies.Com.Arkivanov.MviKotlin.mviKotlinMain)
    implementation(Dependencies.Com.Arkivanov.MviKotlin.mviKotlin)
    implementation(Dependencies.Com.Google.Android.Material.material)
}
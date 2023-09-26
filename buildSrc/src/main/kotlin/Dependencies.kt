object Versions {
    object AndroidX {
        const val compose = "1.5.1"
        const val koin = "3.5.0"
        const val activity = "1.7.2"
        const val core = "1.10.1"
        const val koinCompose = "1.1.0"
    }
}

object Dependencies {
    object AndroidX {
        object Compose {
            object Ui {
                private const val nameSpace = "androidx.compose.ui"
                const val ui = "$nameSpace:ui:${Versions.AndroidX.compose}"
                const val uiTooling = "$nameSpace:ui-tooling:${Versions.AndroidX.compose}"
                const val uiToolingPreview = "$nameSpace:ui-tooling-preview:${Versions.AndroidX.compose}"
            }
            object Foundation {
                private const val nameSpace = "androidx.compose.foundation"
                const val foundation = "$nameSpace:foundation:${Versions.AndroidX.compose}"
            }
            object Material {
                private const val nameSpace = "androidx.compose.material"
                const val material = "$nameSpace:material:${Versions.AndroidX.compose}"
            }
        }
        object Activity {
            private const val nameSpace = "androidx.activity"
            const val activityCompose = "$nameSpace:activity-compose:${Versions.AndroidX.activity}"
        }
        object Core {
            private const val nameSpace = "androidx.core"
            const val core = "$nameSpace:core:${Versions.AndroidX.core}"
        }
    }

    object Io {
        object InsertKoin {
            private const val nameSpace = "io.insert-koin"
            const val koinCore = "$nameSpace:koin-core:${Versions.AndroidX.koin}"
            const val koinAndroid = "$nameSpace:koin-android:${Versions.AndroidX.koin}"
            const val koinCompose = "$nameSpace:koin-compose:${Versions.AndroidX.koinCompose}"
        }
    }
}
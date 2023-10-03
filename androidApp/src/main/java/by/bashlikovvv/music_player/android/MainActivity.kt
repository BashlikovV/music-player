package by.bashlikovvv.music_player.android

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import by.bashlikovvv.music_player.core.presentation.PlayerTheme
import by.bashlikovvv.music_player.ui.root.RootComponent
import by.bashlikovvv.music_player.ui.root.RootContent
import com.arkivanov.decompose.defaultComponentContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsRequest()
        val rootComponent = RootComponent(componentContext = defaultComponentContext())

        setContent {
            PlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RootContent(component = rootComponent)
                }
            }
        }
    }

    private fun permissionsRequest() {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
            permissions.add(android.Manifest.permission.READ_MEDIA_AUDIO)
            permissions.add(android.Manifest.permission.FOREGROUND_SERVICE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            permissions.add(android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK)
        }
        permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        checkPermission(permissions.toTypedArray())
    }

    private fun checkPermission(permissions: Array<String>) {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            it.forEach { (permission, isGranted) ->
                if (!isGranted) {
                    when {
                        ContextCompat.checkSelfPermission(
                            this, permission
                        ) == PackageManager.PERMISSION_GRANTED -> {

                        }
                        shouldShowRequestPermissionRationale(permission) -> {

                        }
                        else -> {
                            this.requestPermissions(arrayOf(permission), 0)
                        }
                    }
                }
            }
        }
        requestPermissionLauncher.launch(permissions)
    }
}

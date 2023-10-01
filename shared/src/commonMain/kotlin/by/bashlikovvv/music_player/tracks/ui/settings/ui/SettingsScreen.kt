package by.bashlikovvv.music_player.tracks.ui.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import by.bashlikovvv.music_player.tracks.ui.settings.SettingsComponent

@Composable
fun SettingsScreen(component: SettingsComponent) {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            Row(modifier = Modifier.fillMaxWidth().clickable { component.onOpenDirectoriesChooser() }) {
                Text("Manage music directories")
            }
        }
    }
}
package by.bashlikovvv.music_player.tracks.ui.chooser.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import by.bashlikovvv.music_player.tracks.ui.chooser.DirectoriesChooserComponent

@Composable
fun DirectoriesChooserScreen(
    component: DirectoriesChooserComponent
) {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            Text("Dir chooser")
        }
    }
}
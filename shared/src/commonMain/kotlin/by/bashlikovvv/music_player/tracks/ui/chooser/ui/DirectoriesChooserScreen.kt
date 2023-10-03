package by.bashlikovvv.music_player.tracks.ui.chooser.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import by.bashlikovvv.music_player.tracks.domain.model.Directory
import by.bashlikovvv.music_player.tracks.ui.chooser.DirectoriesChooserComponent

@Composable
fun DirectoriesChooserScreen(component: DirectoriesChooserComponent) {
    val state by component.state.collectAsState()

    Scaffold(
        topBar = { DirectoriesChooserScreenTopBar(component) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.updateVisibility) {
                CircularProgressIndicator()
            } else {
                LazyColumn(modifier = Modifier.padding(it).fillMaxSize()) {
                    items(state.directories) { item: Directory ->
                        DirectoriesChooserScreenListItem(item)
                    }
                }
            }
        }
    }
}
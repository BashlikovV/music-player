package by.bashlikovvv.music_player.tracks.ui.chooser.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import by.bashlikovvv.music_player.R
import by.bashlikovvv.music_player.tracks.domain.model.Directory
import by.bashlikovvv.music_player.tracks.ui.chooser.DirectoriesChooserComponent

@Composable
fun DirectoriesChooserScreen(component: DirectoriesChooserComponent) {
    val state by component.state.collectAsState()

    Scaffold(
        topBar = { DirectoriesChooserScreenTopBar(component) }
    ) {
        LazyColumn(modifier = Modifier.padding(it).fillMaxSize()) {
            items(state.directories) { item: Directory ->
                DirectoriesChooserScreenListItem(item)
            }
        }
    }
}

@Composable
private fun DirectoriesChooserScreenListItem(directory: Directory) {
    Row(
        modifier = Modifier.fillMaxWidth().height(75.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(directory.path)
    }
}

@Composable
private fun DirectoriesChooserScreenTopBar(component: DirectoriesChooserComponent) {
    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { component.onBackClicked() }) {
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = "navigate back",
                modifier = Modifier.size(20.dp)
            )
        }
        Text("Selecting a folder")
        IconButton(onClick = {  }) {
            Icon(
                painter = painterResource(R.drawable.done),
                contentDescription = "done",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
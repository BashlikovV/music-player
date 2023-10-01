package by.bashlikovvv.music_player.tracks.ui.chooser.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import by.bashlikovvv.music_player.R
import by.bashlikovvv.music_player.tracks.ui.chooser.DirectoriesChooserComponent

@Composable
fun DirectoriesChooserScreenTopBar(component: DirectoriesChooserComponent) {
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
package by.bashlikovvv.music_player.tracks.ui.browser.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import by.bashlikovvv.music_player.R
import by.bashlikovvv.music_player.tracks.ui.browser.ExplorerComponent
import by.bashlikovvv.music_player.tracks.ui.browser.MusicExplorer

@Composable
fun EmptyTracksAlertDialog(component: ExplorerComponent) {
    val state by component.state.collectAsState()
    var checkBoxState by remember { mutableStateOf(false) }

    when {
        state.isTracksEmpty -> {
            Dialog(
                onDismissRequest = {
                    component.onEvent(MusicExplorer.Intent.OnSetIsTracksEmpty(false))
                },
                properties = DialogProperties(dismissOnClickOutside = false)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(
                            horizontal = 5.dp,
                            vertical = 10.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(R.drawable.warning_amber),
                            contentDescription = "Music not found",
                            modifier = Modifier.size(50.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Media files not found.\nTry to set up media directories"
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Show this message again")
                        Checkbox(
                            checked = checkBoxState,
                            onCheckedChange = { checkBoxState = it }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Manage directories",
                            modifier = Modifier
                                .height(50.dp)
                                .width(100.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .clickable { component.onOpenDirectoriesChooser() }
                                .background(MaterialTheme.colorScheme.primary)
                                .wrapContentSize(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Cancel",
                            modifier = Modifier
                                .height(50.dp)
                                .width(100.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .clickable {
                                    component.onEvent(MusicExplorer.Intent.OnSetIsTracksEmpty(false))
                                }
                                .background(MaterialTheme.colorScheme.primary)
                                .wrapContentSize(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
package by.bashlikovvv.music_player.tracks.ui.explorer.ui.modal_bottom_sheet

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import by.bashlikovvv.music_player.R
import by.bashlikovvv.music_player.tracks.ui.explorer.ExplorerComponent
import by.bashlikovvv.music_player.tracks.ui.explorer.MusicExplorer

@Composable
fun ModalBottomSheetMusicControlButtons(component: ExplorerComponent) {
    val state by component.state.collectAsState()
    val playImageState by animateIntAsState(
        if (state.isPlaying) {
            R.drawable.pause
        } else {
            R.drawable.play_arrow
        }, label = ""
    )
    val playRoundState by animateDpAsState(
        if (state.isPlaying) {
            10.dp
        } else {
            25.dp
        }, label = ""
    )

    Row(
        modifier = Modifier.fillMaxWidth().layoutId("controlButtons"),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = {
            component.onEvent(
                MusicExplorer.Intent.OnSelectTrack(
                state.tracks[if (state.tracks.indexOf(state.currentTrack) == 0) {
                    state.tracks.lastIndex
                } else {
                    state.tracks.indexOf(state.currentTrack) - 1
                }]
            ))
            component.onEvent(MusicExplorer.Intent.OnPlayTrack)
        }) {
            Image(
                painter = painterResource(R.drawable.skip_previous),
                contentDescription = "skip previous",
                modifier = Modifier.size(50.dp)
            )
        }
        IconButton(
            onClick = {
                component.onEvent(MusicExplorer.Intent.OnPlayTrack)
            },
            modifier = Modifier.clip(RoundedCornerShape(playRoundState)).background(MaterialTheme.colorScheme.primary)
        ) {
            Image(
                painter = painterResource(playImageState),
                contentDescription = if (state.isPlaying) { "start playing" } else { "stop playing" },
                modifier = Modifier.size(50.dp)
            )
        }
        IconButton(onClick = {
            component.onEvent(
                MusicExplorer.Intent.OnSelectTrack(
                state.tracks[if (state.tracks.indexOf(state.currentTrack) == state.tracks.lastIndex) {
                    0
                } else {
                    state.tracks.indexOf(state.currentTrack) + 1
                }]
            ))
            component.onEvent(MusicExplorer.Intent.OnPlayTrack)
        }) {
            Image(
                painter = painterResource(R.drawable.skip_next),
                contentDescription = "skip next",
                modifier = Modifier.size(50.dp)
            )
        }
    }
}
package by.bashlikovvv.music_player.tracks.ui.browser.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import by.bashlikovvv.music_player.R
import by.bashlikovvv.music_player.tracks.ui.browser.ExplorerComponent
import by.bashlikovvv.music_player.tracks.ui.browser.MusicExplorer

@Composable
fun BrowserScreenBottomBar(
    component: ExplorerComponent,
    onClick: () -> Unit
) {
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
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .layoutId("controlButtons"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(0.6f).padding(start = 5.dp)
        ) {
            Text(
                text = state.currentTrack.getTrackName(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = state.currentTrack.getMusician(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium)
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
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
                if (state.isPlaying) {
                    component.onEvent(MusicExplorer.Intent.OnPlayTrack)
                }
            }) {
                Image(
                    painter = painterResource(R.drawable.skip_previous),
                    contentDescription = "skip previous",
                    modifier = Modifier.size(25.dp)
                )
            }
            IconButton(
                onClick = { component.onEvent(MusicExplorer.Intent.OnPlayTrack) },
                modifier = Modifier.clip(RoundedCornerShape(playRoundState)).background(MaterialTheme.colorScheme.primary)
            ) {
                Image(
                    painter = painterResource(playImageState),
                    contentDescription = if (state.isPlaying) { "start playing" } else { "stop playing" },
                    modifier = Modifier.size(25.dp)
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
                if (state.isPlaying) {
                    component.onEvent(MusicExplorer.Intent.OnPlayTrack)
                }
            }) {
                Image(
                    painter = painterResource(R.drawable.skip_next),
                    contentDescription = "skip next",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}
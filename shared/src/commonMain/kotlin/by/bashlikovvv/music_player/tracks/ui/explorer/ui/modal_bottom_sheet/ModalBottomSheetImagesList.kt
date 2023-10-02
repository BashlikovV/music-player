package by.bashlikovvv.music_player.tracks.ui.explorer.ui.modal_bottom_sheet

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import by.bashlikovvv.music_player.R
import by.bashlikovvv.music_player.tracks.ui.explorer.MusicExplorer
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ModalBottomSheetImagesList(
    lazyListState: LazyListState,
    snapFlingBehavior: FlingBehavior,
    state: MusicExplorer.State
) {
    val track = state.currentTrack

    Column(
        modifier = Modifier.fillMaxWidth().layoutId("imagesList"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp),
            state = lazyListState,
            flingBehavior = snapFlingBehavior,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(state.tracks) {
                Card(modifier = Modifier.fillMaxHeight(0.4f)) {
                    GlideImage(
                        model = track.image,
                        contentDescription = "${track.title} image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxHeight().fillParentMaxWidth()
                    ) {
                        it
                            .error(R.drawable.music_notes)
                            .load(track.image)
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(track.title + " - " + track.artist)
        }
    }
}

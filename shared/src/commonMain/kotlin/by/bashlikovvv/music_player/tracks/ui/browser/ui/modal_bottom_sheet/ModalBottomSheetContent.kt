package by.bashlikovvv.music_player.tracks.ui.browser.ui.modal_bottom_sheet

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import by.bashlikovvv.music_player.tracks.ui.browser.ExplorerComponent
import by.bashlikovvv.music_player.tracks.ui.browser.MusicExplorer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModalBottomSheetContent(
    state: MusicExplorer.State,
    component: ExplorerComponent
) {
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = state.tracks.indexOf(state.currentTrack)
    )
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)

    LaunchedEffect(lazyListState.firstVisibleItemIndex) {
        Log.i("MYTAG", lazyListState.firstVisibleItemIndex.toString())
        val newTrack = state.tracks[lazyListState.firstVisibleItemIndex]
        if (state.currentTrack != newTrack) {
            component.onEvent(MusicExplorer.Intent.OnSelectTrack(newTrack))
        }
        if (state.isPlaying) {
            component.onEvent(MusicExplorer.Intent.OnPlayTrack)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            constraintSet = modalBottomSheetCS(),
            modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp)
        ) {
            ModalBottomSheetTopBar()
            ModalBottomSheetImagesList(lazyListState, snapFlingBehavior, state)
            ModalBottomSheetMusicControlButtons(component)
            ModalBottomSheetMusicIndicators(component)
        }
    }
}

private fun modalBottomSheetCS(): ConstraintSet {
    return ConstraintSet {
        val sheetTopBar = createRefFor("sheetTopBar")
        val imagesList = createRefFor("imagesList")
        val controlButtons = createRefFor("controlButtons")
        val musicIndicators = createRefFor("musicIndicators")

        constrain(sheetTopBar) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(imagesList) {
            top.linkTo(sheetTopBar.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(controlButtons) {
            top.linkTo(imagesList.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(musicIndicators) {
            top.linkTo(controlButtons.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }
    }
}
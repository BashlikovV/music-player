package by.bashlikovvv.music_player.tracks.ui.explorer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import by.bashlikovvv.music_player.core.utils.Constants
import by.bashlikovvv.music_player.tracks.ui.explorer.ExplorerComponent
import by.bashlikovvv.music_player.tracks.ui.explorer.MusicExplorer
import by.bashlikovvv.music_player.tracks.ui.explorer.ui.modal_bottom_sheet.ModalBottomSheetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    component: ExplorerComponent
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    val lazyListState = rememberLazyListState().apply { AddObserver(component) }
    var dropdownMenuState by remember { mutableStateOf(false) }
    val state by component.state.collectAsState()

    EmptyTracksAlertDialog(component)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { BrowserScreenTopBar { dropdownMenuState = !dropdownMenuState } },
        bottomBar = {
            AnimatedVisibility(state.isPlaying || state.currentTrack.id != 0) {
                BrowserScreenBottomBar(component) {
                    openBottomSheet = true
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            BrowserScreenDropdownMenu(component, dropdownMenuState) { dropdownMenuState = false }
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize()
            ) {
                item(key = 0) { ExplorerScreenTracksInfo(component) }
                items(
                    items = state.tracks,
                    key = { state.tracks.indexOf(it) + 1 }
                ) { track ->
                    BrowserScreenMusicItem(track, track == state.currentTrack) {
                        if (state.currentTrack != track) {
                            component.onEvent(MusicExplorer.Intent.OnSelectTrack(it))
                        }
                        component.onEvent(MusicExplorer.Intent.OnPlayTrack)
                    }
                }
                item(key = state.tracks.lastIndex + 2) {
                    if (state.updateVisibility) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) { CircularProgressIndicator() }
                    }
                }
            }
        }

        skipPartiallyExpanded = openBottomSheet

        if (openBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { openBottomSheet = false },
                sheetState = sheetState,
                modifier = Modifier.fillMaxSize()
            ) {
                ModalBottomSheetContent(state, component)
            }
        }
    }
}

@Composable
private fun LazyListState.AddObserver(component: ExplorerComponent) {
    val endOfListReached by remember {
        derivedStateOf { isScrolledToEnd() }
    }

    LaunchedEffect(endOfListReached) {
        if (endOfListReached) {
            component.onEvent(MusicExplorer.Intent.OnLoadBottomTracks())
        }
    }
}

private fun LazyListState.isScrolledToEnd(): Boolean {
    val totalCont = layoutInfo.totalItemsCount - 1
    if (totalCont == 0) { return false }
    val lastItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index

    return lastItemIndex == totalCont
}
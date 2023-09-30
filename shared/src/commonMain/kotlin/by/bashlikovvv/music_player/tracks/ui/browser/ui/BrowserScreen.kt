package by.bashlikovvv.music_player.tracks.ui.browser.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import by.bashlikovvv.music_player.tracks.ui.browser.ExplorerComponent
import by.bashlikovvv.music_player.tracks.ui.browser.MusicExplorer
import by.bashlikovvv.music_player.tracks.ui.browser.ui.modal_bottom_sheet.ModalBottomSheetContent

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
            if (state.isPlaying || state.currentTrack.id != 0) {
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
            LazyColumn(state = lazyListState) {
                items(
                    items = state.tracks,
                    key = { state.tracks.indexOf(it) }
                ) { track ->
                    BrowserScreenMusicItem(track, track == state.currentTrack) {
                        if (state.currentTrack != track) {
                            component.onEvent(MusicExplorer.Intent.OnSelectTrack(it))
                        }
                        component.onEvent(MusicExplorer.Intent.OnPlayTrack)
                    }
                }
                if (state.updateVisibility) {
                    item { CircularProgressIndicator() }
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
        if (endOfListReached) { component.onEvent(MusicExplorer.Intent.OnLoadBottomTracks()) }
    }
}

private fun LazyListState.isScrolledToEnd(): Boolean {
    return layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
}
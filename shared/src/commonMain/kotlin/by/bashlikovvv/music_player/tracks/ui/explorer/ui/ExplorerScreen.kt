package by.bashlikovvv.music_player.tracks.ui.explorer.ui

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import by.bashlikovvv.music_player.R
import by.bashlikovvv.music_player.tracks.ui.explorer.ExplorerComponent
import by.bashlikovvv.music_player.tracks.ui.explorer.MusicExplorer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    component: ExplorerComponent
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    val state by component.state.collectAsState()

    EmptyTracksAlertDialog(component)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { BrowserScreenTopBar() }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                Button(onClick = {
                    openBottomSheet = !openBottomSheet
                }) {
                    Text(text = "Show Bottom Sheet")
                }
            }
            items(state.tracks) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(it.fileName)
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
                ModalBottomSheetContent(state) { openBottomSheet = false }
            }
        }
    }
}

@Composable
private fun BrowserScreenTopBar() {
    Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {  }
}

@Composable
private fun EmptyTracksAlertDialog(component: ExplorerComponent) {
    val state by component.state.collectAsState()

    when {
        state.isTracksEmpty -> {
            Dialog(
                onDismissRequest = {
                    component.onEvent(MusicExplorer.Intent.OnSetIsTracksEmpty(false))
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { component.onOpenDirectoriesChooser() }) {
                        Text("confirm")
                    }
                    Button(onClick = { component.onCloseRequest() }) {
                        Text("dismiss")
                    }
                }
            }
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
            top.linkTo(parent.top)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ModalBottomSheetContent(
    state: MusicExplorer.State,
    onDismissRequest: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            constraintSet = modalBottomSheetCS(),
            modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp)
        ) {
            ModalBottomSheetTopBar(onDismissRequest)
            ModalBottomSheetImagesList(lazyListState, snapFlingBehavior, state)
            ModalBottomSheetMusicControlButtons()
            ModalBottomSheetMusicIndicators()
        }
    }
}

@Composable
private fun ModalBottomSheetImagesList(
    lazyListState: LazyListState,
    snapFlingBehavior: FlingBehavior,
    state: MusicExplorer.State
) {
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
            items((0..10).map { "image $it" }) {
                Card(modifier = Modifier.fillMaxHeight(0.4f)) {
                    Image(
                        painter = painterResource(R.drawable.test_hotel_image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxHeight().fillParentMaxWidth(),
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("state.tracks[state.currentTrackIdx].fileName")
        }
    }
}

@Composable
private fun ModalBottomSheetTopBar(onDismissRequest: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp).layoutId("sheetTopBar"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onDismissRequest) {
            Text("back")
        }
        Button(onClick = {  }) {
            Text("Like")
        }
    }
}

@Composable
private fun ModalBottomSheetMusicControlButtons() {
    var playButtonImage by rememberSaveable { mutableStateOf(false) }
    val playImageState by animateIntAsState(
        if (playButtonImage) R.drawable.play_arrow else R.drawable.play_arrow, label = ""
    )

    Row(
        modifier = Modifier.fillMaxWidth().layoutId("controlButtons"),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(playImageState),
            contentDescription = if (playButtonImage) { "start playing" } else { "stop playing" },
            modifier = Modifier
                .clickable {

                    playButtonImage = !playButtonImage
                }
                .size(50.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ModalBottomSheetMusicIndicators() {
    Row(
        modifier = Modifier.fillMaxWidth().layoutId("musicIndicators"),
        horizontalArrangement = Arrangement.Center
    ) {
        LinearProgressIndicator()
    }
}
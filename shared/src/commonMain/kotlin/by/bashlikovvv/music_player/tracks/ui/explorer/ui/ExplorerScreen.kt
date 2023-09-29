package by.bashlikovvv.music_player.tracks.ui.explorer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import by.bashlikovvv.music_player.R
import by.bashlikovvv.music_player.tracks.domain.model.Track
import by.bashlikovvv.music_player.tracks.ui.explorer.ExplorerComponent
import by.bashlikovvv.music_player.tracks.ui.explorer.MusicExplorer
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    component: ExplorerComponent
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    val lazyListState = rememberLazyListState().apply { AddObserver(component) }
    val state by component.state.collectAsState()

    EmptyTracksAlertDialog(component)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { BrowserScreenTopBar() }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(state = lazyListState) {
                items(state.tracks) { track ->
                    BrowserScreenMusicItem(track) {
                        component.onEvent(MusicExplorer.Intent.OnSelectTrack(it))
                        openBottomSheet = true
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
                ModalBottomSheetContent(state) { openBottomSheet = false }
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun BrowserScreenMusicItem(
    track: Track,
    onClick: (track: Track) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick(track) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = track.imageFilePath,
            contentDescription = "${track.fileName} image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(50.dp).padding(5.dp)
        ) {
            it
                .error(R.drawable.music_notes)
                .load(track.imageFilePath)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.8f)
        ) {
            Text(track.fileName)
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.2f)
        ) {
            IconButton(onClick = {  }) {
                Image(
                    painter = painterResource(R.drawable.menu),
                    contentDescription = "tap to open menu",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

@Composable
private fun BrowserScreenTopBar() {
    var isSearchClicked by rememberSaveable { mutableStateOf(false) }
    val searchContentFraction by animateFloatAsState(
        if (isSearchClicked) {
            0.9f
        } else {
            0f
        }, label = ""
    )

    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Player")
        Row {
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(searchContentFraction)
            )
            IconButton(onClick = { isSearchClicked = !isSearchClicked }) {
                Image(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "tap to search tracks",
                    modifier = Modifier.size(25.dp)
                )
            }
            AnimatedVisibility(!isSearchClicked) {
                IconButton(onClick = {  }) {
                    Image(
                        painter = painterResource(R.drawable.menu),
                        contentDescription = "tap to open menu",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyTracksAlertDialog(component: ExplorerComponent) {
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ModalBottomSheetImagesList(
    lazyListState: LazyListState,
    snapFlingBehavior: FlingBehavior,
    state: MusicExplorer.State
) {
    val track = state.tracks.getOrNull(state.currentTrackIdx)
    track ?: return

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
                        model = track.imageFilePath,
                        contentDescription = "${track.fileName} image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxHeight().fillParentMaxWidth()
                    ) {
                        it
                            .error(R.drawable.music_notes)
                            .load(track.imageFilePath)
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(track.fileName)
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
        if (playButtonImage) {
            R.drawable.play_arrow
        } else {
            R.drawable.pause
        }, label = ""
    )
    val playRoundState by animateDpAsState(
        if (playButtonImage) {
            25.dp
        } else {
            10.dp
        }, label = ""
    )

    Row(
        modifier = Modifier.fillMaxWidth().layoutId("controlButtons"),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = {  }) {
            Icon(
                painter = painterResource(R.drawable.skip_previous),
                contentDescription = "skip previous",
                modifier = Modifier.size(50.dp)
            )
        }
        IconButton(
            onClick = { playButtonImage = !playButtonImage },
            modifier = Modifier
                .clip(RoundedCornerShape(playRoundState))

        ) {
            Icon(
                painter = painterResource(playImageState),
                contentDescription = if (playButtonImage) { "start playing" } else { "stop playing" },
                modifier = Modifier.size(50.dp)
            )
        }
        IconButton(onClick = {  }) {
            Icon(
                painter = painterResource(R.drawable.skip_next),
                contentDescription = "skip next",
                modifier = Modifier.size(50.dp)
            )
        }
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
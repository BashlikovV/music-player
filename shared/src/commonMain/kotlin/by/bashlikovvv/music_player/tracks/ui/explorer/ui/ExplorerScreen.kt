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
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Slider
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
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
import kotlin.math.round
import kotlin.math.roundToInt

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
        topBar = { BrowserScreenTopBar { dropdownMenuState = !dropdownMenuState } }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            ExplorerScreenDropdownMenu(component, dropdownMenuState) { dropdownMenuState = false }
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
                ModalBottomSheetContent(state, component)
            }
        }
    }
}

@Composable
private fun ExplorerScreenDropdownMenu(
    component: ExplorerComponent,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        offset = DpOffset(
            x = 0.dp,
            y = 50.dp
        )
    ) {
        DropdownMenuItem(
            onClick = { component.onOpenSettingsScreen() }
        ) {
            Text("Settings")
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
            Text(
                text = track.fileName,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.2f)
        ) {
            IconButton(onClick = {  }) {
                Image(
                    painter = painterResource(R.drawable.more_vert),
                    contentDescription = "tap to open more",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

@Composable
private fun BrowserScreenTopBar(
    onOpenDropdownMenu: () -> Unit
) {
    var isSearchClicked by rememberSaveable { mutableStateOf(false) }
    val searchContentFraction by animateFloatAsState(
        if (isSearchClicked) {
            0.85f
        } else {
            0f
        }, label = ""
    )
    var textFieldValue by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Player",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 5.dp)
        )
        Row {
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier.fillMaxWidth(searchContentFraction),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                singleLine = true
            )
            IconButton(onClick = { isSearchClicked = !isSearchClicked }) {
                Image(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "tap to search tracks",
                    modifier = Modifier.size(25.dp)
                )
            }
            AnimatedVisibility(!isSearchClicked) {
                IconButton(onClick = { onOpenDropdownMenu() }) {
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
    component: ExplorerComponent
) {
    val lazyListState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)

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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ModalBottomSheetImagesList(
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
private fun ModalBottomSheetTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp).layoutId("sheetTopBar"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

    }
}

@Composable
private fun ModalBottomSheetMusicControlButtons(component: ExplorerComponent) {
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
            component.onEvent(MusicExplorer.Intent.OnSelectTrack(
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
            component.onEvent(MusicExplorer.Intent.OnSelectTrack(
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

@Composable
private fun ModalBottomSheetMusicIndicators(component: ExplorerComponent) {
    val state by component.state.collectAsState()
    val valueRange = 0f..180f
    var value = state.currentTime
    val minutes = value / 60f
    val seconds = round(value % 60)

    Column(
        modifier = Modifier.fillMaxWidth().layoutId("musicIndicators"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Slider(
                value = value,
                onValueChange = { value = it },
                valueRange = valueRange
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text("${minutes.roundToInt()}:${seconds}")
            Text(valueRange.endInclusive.toString())
        }
    }
}
package by.bashlikovvv.music_player.tasks.ui

import by.bashlikovvv.music_player.tasks.domain.model.Track
import by.bashlikovvv.music_player.tasks.ui.explorer.MusicExplorer
import by.bashlikovvv.music_player.tasks.ui.model.BrowserState
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BrowserComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    private val _state = MutableStateFlow(MusicExplorer.State(
        (0..20).map { Track(fileName = "name: $it") }
    ))
    val state: StateFlow<MusicExplorer.State> = _state.asStateFlow()

}
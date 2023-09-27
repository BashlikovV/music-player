package by.bashlikovvv.music_player.tasks.ui.explorer

import by.bashlikovvv.music_player.tasks.domain.model.Track
import com.arkivanov.mvikotlin.core.store.Store

interface MusicExplorer : Store<MusicExplorer.Intent, MusicExplorer.State, Nothing> {

    sealed class Intent {

    }

    data class State(
        val tracks: List<Track> = emptyList(),
    )

}
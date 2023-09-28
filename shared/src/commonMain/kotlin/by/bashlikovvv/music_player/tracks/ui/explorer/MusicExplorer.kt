package by.bashlikovvv.music_player.tracks.ui.explorer

import by.bashlikovvv.music_player.tracks.domain.model.Directory
import by.bashlikovvv.music_player.tracks.domain.model.Track
import com.arkivanov.mvikotlin.core.store.Store

interface MusicExplorer : Store<MusicExplorer.Intent, MusicExplorer.State, Nothing> {

    sealed class Intent {
        data class OnAddDirectory(val directory: Directory): Intent()

        data class OnDeleteDirectory(val directory: Directory) : Intent()

        data class OnUpdateDirectory(val directory: Directory) : Intent()

        data class OnSetIsTracksEmpty(val value: Boolean) : Intent()
    }

    data class State(
        val tracks: List<Track> = emptyList(),
        val currentTrackIdx: Int = 0,
        val isTracksEmpty: Boolean = false
    )

}
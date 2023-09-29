package by.bashlikovvv.music_player.tracks.ui.explorer

import by.bashlikovvv.music_player.core.utils.Constants
import by.bashlikovvv.music_player.tracks.domain.model.Track
import com.arkivanov.mvikotlin.core.store.Store

interface MusicExplorer : Store<MusicExplorer.Intent, MusicExplorer.State, Nothing> {

    sealed class Intent {
        data class OnSetIsTracksEmpty(val value: Boolean) : Intent()

        data class OnSelectTrack(val track: Track) : Intent()

        data class OnLoadBottomTracks(val limit: Int = 0, val offset: Int = Constants.PAGE_SIZE): Intent()
    }

    data class State(
        val tracks: List<Track> = emptyList(),
        val currentTrackIdx: Int = 0,
        val isTracksEmpty: Boolean = false,
        val updateVisibility: Boolean = true,
        val limit: Int = 0,
        val offset: Int = Constants.PAGE_SIZE
    )

}
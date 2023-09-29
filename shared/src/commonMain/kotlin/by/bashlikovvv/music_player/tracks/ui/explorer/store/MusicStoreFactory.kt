package by.bashlikovvv.music_player.tracks.ui.explorer.store

import by.bashlikovvv.music_player.core.utils.Constants
import by.bashlikovvv.music_player.tracks.domain.model.Track
import by.bashlikovvv.music_player.tracks.domain.repository.ITracksRepository
import by.bashlikovvv.music_player.tracks.ui.explorer.MusicExplorer
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MusicStoreFactory(
    private val storeFactory: StoreFactory
) : KoinComponent {

    private val tracksRepository: ITracksRepository by inject()

    fun create(): MusicExplorer =
        object : MusicExplorer,
            Store<MusicExplorer.Intent, MusicExplorer.State, Nothing> by storeFactory.create(
                name = STORE_NAME,
                initialState = MusicExplorer.State(),
                bootstrapper = SimpleBootstrapper(Action.LoadTracks()),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {
        data class TracksLoaded(val tracks: List<Track>) : Msg()

        data class IsTracksIsEmpty(val value: Boolean) : Msg()

        data class TrackSelected(val track: Track) : Msg()

        data class UpdateVisibilityChanged(val updateVisibility: Boolean) : Msg()

        data object IncrementLimit : Msg()

    }

    private inner class ExecutorImpl :
        CoroutineExecutor<MusicExplorer.Intent, Action, MusicExplorer.State, Msg, Nothing>(
            Dispatchers.Main
        ) {
            override fun executeIntent(
                intent: MusicExplorer.Intent,
                getState: () -> MusicExplorer.State
            ) {
                when(intent) {
                    is MusicExplorer.Intent.OnSetIsTracksEmpty -> dispatch(
                        Msg.IsTracksIsEmpty(value = intent.value)
                    )
                    is MusicExplorer.Intent.OnSelectTrack -> selectTrack(track = intent.track)
                    is MusicExplorer.Intent.OnLoadBottomTracks -> loadTracks(
                        limit = getState().limit,
                        offset = intent.offset
                    ) { Msg.IncrementLimit }
                }
            }

            override fun executeAction(action: Action, getState: () -> MusicExplorer.State) {
                when (action) {
                    is Action.LoadTracks -> loadTracks(action.limit, action.offset)
                }
            }

            private fun loadTracks(
                limit: Int,
                offset: Int,
                dispatchSuccessMessage: ((List<Track>) -> Msg)? = null
            ) {
                scope.launch {
                    dispatch(Msg.UpdateVisibilityChanged(true))
                    val tracks = tracksRepository.loadTracks(limit, offset)
                    tracks.collectLatest {
                        if (it.isNotEmpty()) {
                            dispatch(Msg.TracksLoaded(it))
                            if (dispatchSuccessMessage != null) {
                                dispatch(dispatchSuccessMessage(it))
                            }
                        } else {
                            dispatch(Msg.IsTracksIsEmpty(true))
                        }
                    }
                }.invokeOnCompletion { dispatch(Msg.UpdateVisibilityChanged(false)) }
            }

            private fun selectTrack(track: Track) { dispatch(Msg.TrackSelected(track)) }
        }

    private object ReducerImpl : Reducer<MusicExplorer.State, Msg> {
        override fun MusicExplorer.State.reduce(msg: Msg): MusicExplorer.State = when (msg) {
            is Msg.TracksLoaded -> copy(tracks = this.tracks + msg.tracks, isTracksEmpty = false, updateVisibility = false)
            is Msg.IsTracksIsEmpty -> copy(isTracksEmpty = msg.value)
            is Msg.TrackSelected -> copy(currentTrackIdx = msg.track.id)
            is Msg.UpdateVisibilityChanged -> copy(updateVisibility = msg.updateVisibility)
            is Msg.IncrementLimit -> copy(limit = limit + Constants.PAGE_SIZE)
        }
    }

    sealed class Action {
        class LoadTracks(val limit: Int = 0, val offset: Int = Constants.PAGE_SIZE) : Action()
    }

    companion object {
        const val STORE_NAME = "MusicStore"
    }
}
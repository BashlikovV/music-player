package by.bashlikovvv.music_player.tracks.ui.explorer.store

import by.bashlikovvv.music_player.tracks.domain.model.Directory
import by.bashlikovvv.music_player.tracks.domain.model.Track
import by.bashlikovvv.music_player.tracks.domain.repository.IDirectoriesRepository
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

    private val directoriesRepository: IDirectoriesRepository by inject()

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
                    is MusicExplorer.Intent.OnAddDirectory -> addDirectory(intent.directory)
                    is MusicExplorer.Intent.OnDeleteDirectory -> deleteDirectory(intent.directory)
                    is MusicExplorer.Intent.OnUpdateDirectory -> updateDirectory(intent.directory)
                    is MusicExplorer.Intent.OnSetIsTracksEmpty -> dispatch(
                        Msg.IsTracksIsEmpty(
                            intent.value
                        )
                    )
                }
            }

            override fun executeAction(action: Action, getState: () -> MusicExplorer.State) {
                when(action) {
                    is Action.LoadTracks -> loadTracks(action.limit, action.offset)
                }
            }

            private fun updateDirectory(directory: Directory) {
                scope.launch { directoriesRepository.updateDirectory(directory) }
            }

            private fun addDirectory(directory: Directory) {
                scope.launch { directoriesRepository.addDirectories(listOf(directory)) }
            }

            private fun deleteDirectory(directory: Directory) {
                scope.launch { directoriesRepository.deleteDirectory(directory) }
            }

            private fun loadTracks(limit: Int, offset: Int) {
                scope.launch {
                    val tracks = tracksRepository.loadTracks(limit, offset)
                    tracks.collectLatest {
                        if (it.isNotEmpty()) {
                            dispatch(Msg.TracksLoaded(it))
                        } else {
                            dispatch(Msg.IsTracksIsEmpty(true))
                        }
                    }
                }
            }
        }

    private object ReducerImpl : Reducer<MusicExplorer.State, Msg> {
        override fun MusicExplorer.State.reduce(msg: Msg): MusicExplorer.State = when (msg) {
            is Msg.TracksLoaded -> copy(tracks = msg.tracks, isTracksEmpty = false)
            is Msg.IsTracksIsEmpty -> copy(isTracksEmpty = msg.value)
        }
    }

    sealed class Action {
        class LoadTracks(val limit: Int = 0, val offset: Int = 30) : Action()
    }

    companion object {
        const val STORE_NAME = "MusicStore"
    }
}
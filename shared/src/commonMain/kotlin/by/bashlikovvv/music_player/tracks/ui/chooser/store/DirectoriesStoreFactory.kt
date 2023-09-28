package by.bashlikovvv.music_player.tracks.ui.chooser.store

import by.bashlikovvv.music_player.tracks.domain.model.Directory
import by.bashlikovvv.music_player.tracks.ui.chooser.DirectoriesChooserExplorer
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent

class DirectoriesStoreFactory(
    private val storeFactory: StoreFactory
) : KoinComponent {

    fun create(): DirectoriesChooserExplorer =
        object : DirectoriesChooserExplorer,
            Store<DirectoriesChooserExplorer.Intent, DirectoriesChooserExplorer.State, Nothing> by storeFactory.create(
                name = STORE_NAME,
                initialState = DirectoriesChooserExplorer.State(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {

        data class DirectoriesLoaded(val directories: List<Directory>) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<DirectoriesChooserExplorer.Intent, Action, DirectoriesChooserExplorer.State, Msg, Nothing>(
            Dispatchers.Main
        ) {

        }

    private object ReducerImpl : Reducer<DirectoriesChooserExplorer.State, Msg> {
        override fun DirectoriesChooserExplorer.State.reduce(msg: Msg): DirectoriesChooserExplorer.State =
            when(msg) {
                is Msg.DirectoriesLoaded -> copy(directories = msg.directories)
            }
    }

    sealed class Action {
        class LoadTracks() : Action()
    }

    companion object {
        const val STORE_NAME = "DirectoriesStore"
    }

}
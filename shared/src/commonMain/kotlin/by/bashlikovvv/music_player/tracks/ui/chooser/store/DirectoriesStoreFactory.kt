package by.bashlikovvv.music_player.tracks.ui.chooser.store

import by.bashlikovvv.music_player.tracks.data.mapper.CommonDirectoryEntityMapper
import by.bashlikovvv.music_player.tracks.data.model.DirectoryEntity
import by.bashlikovvv.music_player.tracks.domain.model.Directory
import by.bashlikovvv.music_player.tracks.domain.repository.IDirectoriesRepository
import by.bashlikovvv.music_player.tracks.ui.chooser.DirectoriesChooserExplorer
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

class DirectoriesStoreFactory(
    private val storeFactory: StoreFactory
) : KoinComponent {

    private val directoriesRepository: IDirectoriesRepository by inject()

    fun create(): DirectoriesChooserExplorer =
        object : DirectoriesChooserExplorer,
            Store<DirectoriesChooserExplorer.Intent, DirectoriesChooserExplorer.State, Nothing> by storeFactory.create(
                name = STORE_NAME,
                initialState = DirectoriesChooserExplorer.State(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
                bootstrapper = SimpleBootstrapper(Action.OnScanDeviceAction)
            ) {  }

    private sealed class Msg {

        data class DirectoriesLoaded(val directories: List<Directory>) : Msg()

        data object ClearDirectories : Msg()

        data class UpdateVisibilityChanged(val updateVisibility: Boolean) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<DirectoriesChooserExplorer.Intent, Action, DirectoriesChooserExplorer.State, Msg, Nothing>(
            Dispatchers.Main
        ) {
            override fun executeIntent(
                intent: DirectoriesChooserExplorer.Intent,
                getState: () -> DirectoriesChooserExplorer.State
            ) {
                when(intent) {
                    is DirectoriesChooserExplorer.Intent.OnAddDirectories -> addDirectory(intent.directory)
                    is DirectoriesChooserExplorer.Intent.OnStartDeviceScanning -> startDeviceScanning()
                }
            }

            override fun executeAction(
                action: Action,
                getState: () -> DirectoriesChooserExplorer.State
            ) {
                when (action) {
                    is Action.OnScanDeviceAction -> startDeviceScanning()
                }
            }

            private fun addDirectory(directory: Directory) {
                scope.launch { directoriesRepository.addDirectories(listOf(directory)) }
            }

            private fun startDeviceScanning() {
                scope.launch {
                    dispatch(Msg.UpdateVisibilityChanged(true))
                    directoriesRepository.scanDevice()

                    directoriesRepository.getDirectories().collectLatest { directoryEntities ->
                        val directories = directoryEntities.map {
                            CommonDirectoryEntityMapper().toDomain(it as DirectoryEntity.CommonDirectoryEntity)
                        }
                        dispatch(Msg.ClearDirectories)
                        dispatch(Msg.DirectoriesLoaded(directories))
                    }
                }
            }
    }

    private object ReducerImpl : Reducer<DirectoriesChooserExplorer.State, Msg> {
        override fun DirectoriesChooserExplorer.State.reduce(msg: Msg): DirectoriesChooserExplorer.State =
            when(msg) {
                is Msg.DirectoriesLoaded -> copy(directories = msg.directories, updateVisibility = false)
                is Msg.ClearDirectories -> copy(directories = emptyList())
                is Msg.UpdateVisibilityChanged -> copy(updateVisibility = msg.updateVisibility)
            }
    }

    sealed class Action {

        data object OnScanDeviceAction : Action()

    }

    companion object {
        const val STORE_NAME = "DirectoriesStore"
    }

}
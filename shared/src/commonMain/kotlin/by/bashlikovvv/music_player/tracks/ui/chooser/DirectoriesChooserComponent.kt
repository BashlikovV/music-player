package by.bashlikovvv.music_player.tracks.ui.chooser

import by.bashlikovvv.music_player.tracks.ui.chooser.model.IItemDirectoriesChooser
import by.bashlikovvv.music_player.tracks.ui.chooser.store.DirectoriesStoreFactory
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.flow.StateFlow

class DirectoriesChooserComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val onClose: () -> Unit
) : IItemDirectoriesChooser, ComponentContext by componentContext {

    private val directoriesStore = instanceKeeper.getStore {
        DirectoriesStoreFactory(storeFactory).create()
    }

    val state: StateFlow<DirectoriesChooserExplorer.State> = directoriesStore.stateFlow

    fun onEvent(event: DirectoriesChooserExplorer.Intent) {
        directoriesStore.accept(event)
    }

    override fun onBackClicked() {
        onClose.invoke()
    }
}
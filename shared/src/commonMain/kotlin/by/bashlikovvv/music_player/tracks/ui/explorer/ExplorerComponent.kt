package by.bashlikovvv.music_player.tracks.ui.explorer

import by.bashlikovvv.music_player.tracks.ui.explorer.model.IItemExplorer
import by.bashlikovvv.music_player.tracks.ui.explorer.store.MusicStoreFactory
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class ExplorerComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val onOpenDirectoriesChooser: () -> Unit,
    private val onOpenSettingsScreen: () -> Unit
) : IItemExplorer, ComponentContext by componentContext {

    private val musicStore = instanceKeeper.getStore {
        MusicStoreFactory(storeFactory).create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<MusicExplorer.State> = musicStore.stateFlow

    fun onEvent(event: MusicExplorer.Intent) {
        musicStore.accept(event)
    }

    override fun onOpenDirectoriesChooser() { onOpenDirectoriesChooser.invoke() }
    override fun onOpenSettingsScreen() { onOpenSettingsScreen.invoke() }
}
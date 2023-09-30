package by.bashlikovvv.music_player.tracks.ui.settings

import by.bashlikovvv.music_player.tracks.ui.settings.model.IItemSettings
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory

class SettingsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val onClose: () -> Unit
) : IItemSettings, ComponentContext by componentContext {

    override fun onBackClicked() { onClose.invoke() }

}
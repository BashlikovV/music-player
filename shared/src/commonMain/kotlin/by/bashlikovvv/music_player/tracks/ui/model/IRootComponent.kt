package by.bashlikovvv.music_player.tracks.ui.model

import by.bashlikovvv.music_player.tracks.ui.chooser.DirectoriesChooserComponent
import by.bashlikovvv.music_player.tracks.ui.explorer.ExplorerComponent
import by.bashlikovvv.music_player.tracks.ui.settings.SettingsComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface IRootComponent {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class ExplorerChild(val component: ExplorerComponent) : Child()
        class ChooserChild(val component: DirectoriesChooserComponent) : Child()
        class SettingsChild(val component: SettingsComponent) : Child()
    }

}
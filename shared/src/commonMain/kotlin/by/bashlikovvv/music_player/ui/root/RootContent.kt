package by.bashlikovvv.music_player.ui.root

import androidx.compose.runtime.Composable
import by.bashlikovvv.music_player.tracks.ui.explorer.ui.BrowserScreen
import by.bashlikovvv.music_player.tracks.ui.chooser.ui.DirectoriesChooserScreen
import by.bashlikovvv.music_player.tracks.ui.settings.ui.SettingsScreen
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation

@Composable
fun RootContent(
    component: RootComponent
) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(slide())
    ) {
        when(val child = it.instance) {
            is IRootComponent.Child.ExplorerChild -> BrowserScreen(child.component)
            is IRootComponent.Child.ChooserChild -> DirectoriesChooserScreen(child.component)
            is IRootComponent.Child.SettingsChild -> SettingsScreen(child.component)
        }
    }
}
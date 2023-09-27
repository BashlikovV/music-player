package by.bashlikovvv.music_player.ui.root

import androidx.compose.runtime.Composable
import by.bashlikovvv.music_player.tasks.ui.BrowserScreen
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation

@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade())
    ) {
        when(val child = it.instance) {
            is RootComponent.Child.Browser -> BrowserScreen(child.component)
        }
    }
}
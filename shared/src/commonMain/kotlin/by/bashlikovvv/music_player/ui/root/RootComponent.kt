package by.bashlikovvv.music_player.ui.root

import by.bashlikovvv.music_player.tracks.ui.chooser.DirectoriesChooserComponent
import by.bashlikovvv.music_player.tracks.ui.explorer.ExplorerComponent
import by.bashlikovvv.music_player.tracks.ui.settings.SettingsComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory

class RootComponent(
    componentContext: ComponentContext
) : IRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()

    override val childStack: Value<ChildStack<*, IRootComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Configuration.Browser,
            handleBackButton = true,
            childFactory = ::createChild
        )

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): IRootComponent.Child =
        when (configuration) {
            Configuration.Browser -> IRootComponent.Child.ExplorerChild(itemBrowser(componentContext))
            Configuration.DirectoriesChooser -> IRootComponent.Child.ChooserChild(itemDirectoriesChooser(componentContext))
            Configuration.Settings -> IRootComponent.Child.SettingsChild(itemSettings(componentContext))
        }

    private fun itemBrowser(componentContext: ComponentContext): ExplorerComponent {
        return ExplorerComponent(
            componentContext = componentContext,
            storeFactory = DefaultStoreFactory(),
            onOpenDirectoriesChooser = { navigation.push(Configuration.DirectoriesChooser) },
            onOpenSettingsScreen = { navigation.push(Configuration.Settings) }
        )
    }

    private fun itemDirectoriesChooser(componentContext: ComponentContext): DirectoriesChooserComponent {
        return DirectoriesChooserComponent(
            componentContext = componentContext,
            storeFactory = DefaultStoreFactory(),
            onClose = { navigation.pop() }
        )
    }

    private fun itemSettings(componentContext: ComponentContext): SettingsComponent {
        return SettingsComponent(
            componentContext = componentContext,
            storeFactory = DefaultStoreFactory(),
            onClose = { navigation.pop() },
            onOpenDirectoriesChooser = { navigation.push(Configuration.DirectoriesChooser) }
        )
    }

    private sealed class Configuration: Parcelable {
        @Parcelize
        data object Browser : Configuration()

        @Parcelize
        data object DirectoriesChooser : Configuration()

        @Parcelize
        data object Settings : Configuration()
    }
}
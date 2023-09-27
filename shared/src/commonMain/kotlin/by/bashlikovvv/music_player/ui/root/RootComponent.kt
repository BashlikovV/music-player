package by.bashlikovvv.music_player.ui.root

import by.bashlikovvv.music_player.tasks.ui.BrowserComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize

class RootComponent(
    componentContext: ComponentContext,
    private val tasks: (ComponentContext) -> BrowserComponent
) : ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
    ) : this(
        componentContext = componentContext,
        tasks = { childContext ->
            BrowserComponent(componentContext = childContext)
        }
    )

    private val navigation = StackNavigation<Configuration>()

    private val stack = childStack(
        source = navigation,
        initialConfiguration = Configuration.Tasks,
        handleBackButton = true,
        childFactory = ::createChild
    )

    val childStack: Value<ChildStack<*, Child>> = stack

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): Child =
        when (configuration) {
            Configuration.Tasks -> Child.Browser(tasks(componentContext))
        }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Tasks : Configuration()
    }

    sealed class Child {
        data class Browser(val component: BrowserComponent) : Child()
    }
}
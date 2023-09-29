package by.bashlikovvv.music_player.tracks.ui.chooser

import by.bashlikovvv.music_player.tracks.domain.model.Directory
import com.arkivanov.mvikotlin.core.store.Store

interface DirectoriesChooserExplorer :
    Store<DirectoriesChooserExplorer.Intent, DirectoriesChooserExplorer.State, Nothing> {

    sealed class Intent {
        data class OnAddDirectories(val directory: Directory): Intent()

        data object OnStartDeviceScanning : Intent()
    }

    data class State(val directories: List<Directory> = emptyList())

}
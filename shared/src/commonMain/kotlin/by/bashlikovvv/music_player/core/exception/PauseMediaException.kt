package by.bashlikovvv.music_player.core.exception

class PauseMediaException : RuntimeException() {

    override val message: String
        get() = MESSAGE

    companion object {
        const val MESSAGE = "Can not pause current media exception"
    }

}
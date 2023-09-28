package by.bashlikovvv.music_player.core.exception

class PlayMediaException : RuntimeException() {

    override val message: String
        get() = MESSAGE

    companion object {
        const val MESSAGE = "Can not play current media exception"
    }

}
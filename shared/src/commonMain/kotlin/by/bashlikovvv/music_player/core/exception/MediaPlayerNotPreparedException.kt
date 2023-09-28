package by.bashlikovvv.music_player.core.exception

class MediaPlayerNotPreparedException(cause: Throwable? = null) : RuntimeException(cause) {

    override val message: String
        get() = MESSAGE

    companion object {
        const val MESSAGE = "Media player not prepared exception"
    }

}
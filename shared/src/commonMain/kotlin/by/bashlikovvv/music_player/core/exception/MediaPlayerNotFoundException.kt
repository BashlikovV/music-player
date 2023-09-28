package by.bashlikovvv.music_player.core.exception

class MediaPlayerNotFoundException : RuntimeException() {

    override val message: String
        get() = MESSAGE

    companion object {
        const val MESSAGE = "Media player not found exception"
    }

}
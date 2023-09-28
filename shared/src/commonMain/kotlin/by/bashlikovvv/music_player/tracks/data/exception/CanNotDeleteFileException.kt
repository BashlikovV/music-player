package by.bashlikovvv.music_player.tracks.data.exception

class CanNotDeleteFileException : RuntimeException() {

    override val message: String?
        get() = MESSAGE

    companion object {
        const val MESSAGE = "Can not delete file exception"
    }

}
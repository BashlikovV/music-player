package by.bashlikovvv.music_player.core.data

expect class AudioPlayer {

    fun preparePlayer(path: String, filename: String)

    fun playCurrentMedia()

    fun pauseCurrentMedia()

    fun resetPlayer()
}
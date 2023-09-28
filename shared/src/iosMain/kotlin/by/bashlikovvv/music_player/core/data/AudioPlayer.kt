package by.bashlikovvv.music_player.core.data

import platform.AVFoundation

actual class AudioPlayer {

    private var _mediaPlayer: AVAudioPlayer? = null
    val mediaPlayer = _mediaPlayer

    fun preparePlayer(path: String, filename: String) {

    }

    fun playCurrentMedia() {

    }

    fun pauseCurrentMedia() {

    }

    fun resetPlayer() {

    }

}
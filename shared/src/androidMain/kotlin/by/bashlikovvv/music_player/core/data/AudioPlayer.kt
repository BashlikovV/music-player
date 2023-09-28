package by.bashlikovvv.music_player.core.data

import android.media.MediaPlayer
import by.bashlikovvv.music_player.core.exception.MediaPlayerNotFoundException
import by.bashlikovvv.music_player.core.exception.MediaPlayerNotPreparedException
import by.bashlikovvv.music_player.core.exception.PauseMediaException
import by.bashlikovvv.music_player.core.exception.PlayMediaException
import java.io.File
import java.io.IOException

actual class AudioPlayer {

    private var _mediaPlayer: MediaPlayer? = null
    val mediaPlayer = _mediaPlayer

    /**
     * @throws MediaPlayerNotFoundException
     * */
    actual fun preparePlayer(path: String, filename: String) {
        if (_mediaPlayer != null) {
            _mediaPlayer!!.reset()
        } else {
            _mediaPlayer = MediaPlayer()
        }

        try {
            _mediaPlayer!!.setDataSource(path + File.separator + filename)
            _mediaPlayer!!.prepare()
        } catch (e: IllegalStateException) {
            throw MediaPlayerNotPreparedException(e.cause)
        } catch (e: SecurityException) {
            throw MediaPlayerNotPreparedException(e.cause)
        } catch (e: IllegalArgumentException) {
            throw MediaPlayerNotPreparedException(e.cause)
        } catch (e: IOException) {
            throw MediaPlayerNotPreparedException(e.cause)
        }
    }

    /**
     * @throws MediaPlayerNotFoundException if media player was not prepared
     * or an error has occurred during preparing media player
     * @throws PlayMediaException if it is called in an invalid state
     * */
    actual fun playCurrentMedia() {
        if (_mediaPlayer == null) { throw MediaPlayerNotFoundException() }

        try {
            _mediaPlayer!!.start()
        } catch (e: IllegalStateException) {
            throw PlayMediaException()
        }
    }

    /**
     * @throws MediaPlayerNotFoundException if media player was not prepared
     * or an error has occurred during preparing media player
     * @throws PauseMediaException if it is called in an invalid state
     * */
    actual fun pauseCurrentMedia() {
        if (_mediaPlayer == null) { throw MediaPlayerNotFoundException() }

        try {
            _mediaPlayer!!.pause()
        } catch (e: IllegalStateException) {
            throw PauseMediaException()
        }
    }

    actual fun resetPlayer() {
        if (_mediaPlayer != null) { _mediaPlayer!!.reset() }
    }

}
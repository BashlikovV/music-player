package by.bashlikovvv.music_player.tracks.domain.model

import android.graphics.Bitmap
import java.util.concurrent.TimeUnit

data class Track(
    val id: Int = 0,
    val trackFilePath: String = "",
    val fileName: String = "",
    val isCurrent: Boolean = false,
    var duration: String = "",
    val artist: String = "",
    val title: String = "",
    val image: Bitmap? = null
) {

    fun durationToStringFormat() = try {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
        val tmp = seconds - minutes * 60
        val secondsString = if (tmp.toString().length == 1) {
            "0$tmp"
        } else {
            tmp.toString()
        }
        "$minutes:$secondsString"
    } catch (e: NumberFormatException) {
        duration
    }

    fun durationToFloatFormat() = try {
        TimeUnit.MILLISECONDS.toSeconds(duration.toLong()).toFloat()
    } catch (e: NumberFormatException) {
        0f
    }
}
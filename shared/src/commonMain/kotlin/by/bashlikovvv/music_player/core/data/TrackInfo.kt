package by.bashlikovvv.music_player.core.data

import android.graphics.Bitmap

expect class TrackInfo {

    fun setDataSource(path: String)

    fun getTitle(): String?

    fun getDuration(): String?

    fun getArtist(): String?

    fun getImage(): Bitmap?
}
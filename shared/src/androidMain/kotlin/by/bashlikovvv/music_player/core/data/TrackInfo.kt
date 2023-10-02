package by.bashlikovvv.music_player.core.data

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import androidx.annotation.RequiresApi

actual class TrackInfo {

    private val mediaMetadataRetriever = MediaMetadataRetriever()

    actual fun setDataSource(path: String) = mediaMetadataRetriever.setDataSource(path)

    actual fun getTitle() = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

    actual fun getDuration(): String? = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

    actual fun getArtist(): String? = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

    @RequiresApi(Build.VERSION_CODES.P)
    actual fun getImage(): Bitmap? {
        return try {
            mediaMetadataRetriever.primaryImage
        } catch (e: IllegalStateException) {
            null
        }
    }
}
package by.bashlikovvv.music_player.core.data

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build

actual class TrackInfo {

    private val mediaMetadataRetriever = MediaMetadataRetriever()

    actual fun setDataSource(path: String) = mediaMetadataRetriever.setDataSource(path)

    actual fun getTitle() = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

    actual fun getDuration(): String? = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

    actual fun getArtist(): String? = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

    actual fun getImage(): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mediaMetadataRetriever.primaryImage
            } else {
                null
            }
        } catch (e: IllegalStateException) {
            null
        }
    }
}
package by.bashlikovvv.music_player.core.data

import by.bashlikovvv.music_player.tracks.domain.model.Directory
import java.io.File

expect class StorageScanner {

    fun scanDevice(): List<File>

    fun scanDirectory(
        directory: Directory,
        limit: Int,
        offset: Int
    ): List<File>

}
package by.bashlikovvv.music_player.core.data

import android.os.Environment
import by.bashlikovvv.music_player.tracks.domain.model.Directory
import java.io.File

actual class StorageScanner {

    private val directories = mutableSetOf<File>()

    actual fun scanDevice(): List<File> {
        scanDefaultStorages(Environment.getExternalStorageDirectory().path)

        return directories.toList()
    }

    actual fun scanDirectory(
        directory: Directory,
        limit: Int,
        offset: Int
    ): List<File> {
        val files = mutableListOf<File?>()

        val tmp = File(directory.path).listFiles()
        if (tmp != null) {
            for (i in limit until limit + offset step 1) {
                files.add(tmp.getOrNull(i))
            }
        }

        return files.filterNotNull()
    }

    private fun scanDefaultStorages(pathParameter: String) {
        val listFiles = File(pathParameter).listFiles()
        if (listFiles.isNullOrEmpty()) { return }

        for (root in listFiles) {
            if (root.isDirectory) {
                scanDefaultStorages(root.path)
            } else {
                if (directories.contains(File(pathParameter))) { continue }
                if (root.extension == "mp3") { directories.add(File(pathParameter)) }
            }
        }
    }
}
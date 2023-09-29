package by.bashlikovvv.music_player.tracks.domain.repository

import by.bashlikovvv.music_player.tracks.data.model.DirectoryEntity
import by.bashlikovvv.music_player.tracks.domain.model.Directory
import kotlinx.coroutines.flow.Flow

interface IDirectoriesRepository {

    fun getDirectories(): Flow<List<DirectoryEntity>>

    suspend fun updateDirectory(directory: Directory)

    suspend fun addDirectories(directories: List<Directory>)

    suspend fun deleteDirectory(directory: Directory)

    suspend fun scanDevice()
}
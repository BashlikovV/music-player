package by.bashlikovvv.music_player.tracks.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import by.bashlikovvv.music_player.core.data.StorageScanner
import by.bashlikovvv.music_player.tracks.data.mapper.FileToDirectoryMapper
import by.bashlikovvv.music_player.tracks.data.mapper.PathEntityMapper
import by.bashlikovvv.music_player.tracks.data.model.DirectoryEntity
import by.bashlikovvv.music_player.tracks.domain.model.Directory
import by.bashlikovvv.music_player.tracks.domain.repository.IDirectoriesRepository
import by.bashlikovvv.music_player.tracksDatabase.TracksDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.CoroutineContext

class DirectoriesRepositoryImpl(
    db: TracksDatabase,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) : IDirectoriesRepository {

    private val queries = db.pathsQueries

    override fun getDirectories(): Flow<List<DirectoryEntity>> {
        return queries
            .getDirectoriesTask()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { pathEntities ->
                supervisorScope {
                    pathEntities.map {
                        async { PathEntityMapper().toDomain(it) }
                    }.map { it.await() }
                }
            }
    }

    override suspend fun updateDirectory(directory: Directory) {
        queries.updateDirectoryTask(
            newMusicDirPath = directory.path,
            id = directory.id.toLong()
        )
    }

    override suspend fun addDirectories(directories: List<Directory>) {
        directories.forEach { queries.insertDirectoryTask(it.path) }
    }

    override suspend fun deleteDirectory(directory: Directory) {
        queries.deleteDirectoryTask(id = directory.id.toLong())
    }

    override suspend fun scanDevice() {
        var id = 0
        val directories = StorageScanner().scanDevice().map {
            FileToDirectoryMapper(id).toDomain(it).also { id++ }
        }
        addDirectories(directories)
    }
}
package by.bashlikovvv.music_player.tracks.data.repository

import by.bashlikovvv.music_player.tracks.data.exception.CanNotDeleteFileException
import by.bashlikovvv.music_player.tracks.data.mapper.CommonDirectoryEntityMapper
import by.bashlikovvv.music_player.tracks.data.mapper.DirectoryMapper
import by.bashlikovvv.music_player.tracks.data.model.DirectoryEntity
import by.bashlikovvv.music_player.tracks.domain.model.Track
import by.bashlikovvv.music_player.tracks.domain.repository.IDirectoriesRepository
import by.bashlikovvv.music_player.tracks.domain.repository.ITracksRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope
import java.io.File

class TracksRepositoryImpl(
    private val directoriesRepository: IDirectoriesRepository
) : ITracksRepository {

    override fun loadTracks(limit: Int, offset: Int): Flow<List<Track>> {
        return directoriesRepository.getDirectories().map { directoryEntities ->
            supervisorScope {
                directoryEntities.map {
                    async {
                        DirectoryMapper().toDomain(
                            CommonDirectoryEntityMapper().toDomain(it as DirectoryEntity.CommonDirectoryEntity)
                        )
                    }
                }.map { it.await() }
            }
        }
    }

    /**
     * @throws CanNotDeleteFileException if track file not found
     * */
    override suspend fun deleteTrack(track: Track) {
        val tracks = directoriesRepository.getDirectories().map { directoryEntities ->
            directoryEntities.map {
                DirectoryMapper().toDomain(
                    CommonDirectoryEntityMapper().toDomain(it as DirectoryEntity.CommonDirectoryEntity)
                )
            }.map { it }
        }

        tracks.map { tracks ->
            tracks.forEach {
                if (it == track) {
                    try {
                        File(track.trackFilePath + File.separator + track.fileName).delete()
                    } catch (e: NullPointerException) {
                        throw CanNotDeleteFileException()
                    } catch (e: SecurityException) {
                        throw CanNotDeleteFileException()
                    }
                }
            }
        }
    }
}
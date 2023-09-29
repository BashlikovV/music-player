package by.bashlikovvv.music_player.tracks.data.repository

import by.bashlikovvv.music_player.core.data.StorageScanner
import by.bashlikovvv.music_player.tracks.data.exception.CanNotDeleteFileException
import by.bashlikovvv.music_player.tracks.data.mapper.CommonDirectoryEntityMapper
import by.bashlikovvv.music_player.tracks.data.mapper.DirectoryMapper
import by.bashlikovvv.music_player.tracks.data.mapper.FileToDirectoryMapper
import by.bashlikovvv.music_player.tracks.data.model.DirectoryEntity
import by.bashlikovvv.music_player.tracks.domain.model.Track
import by.bashlikovvv.music_player.tracks.domain.repository.IDirectoriesRepository
import by.bashlikovvv.music_player.tracks.domain.repository.ITracksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.supervisorScope
import java.io.File

class TracksRepositoryImpl(
    private val directoriesRepository: IDirectoriesRepository
) : ITracksRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun loadTracks(limit: Int, offset: Int): Flow<List<Track>> {
        val storageScanner = StorageScanner()

        val tmpRes = directoriesRepository.getDirectories().mapLatest { directoryEntities ->
            var id = 0

            supervisorScope {
                val tracks = mutableListOf<Track>()

                directoryEntities.map {
                    async {
                        storageScanner.scanDirectory(
                            directory = CommonDirectoryEntityMapper().toDomain(it as DirectoryEntity.CommonDirectoryEntity),
                            limit = limit,
                            offset = offset
                        ).map {
                            DirectoryMapper().toDomain(
                                FileToDirectoryMapper(id).toDomain(it).also { id++ }
                            )
                        }
                    }
                }.map { tracks.addAll(it.await()) }

                tracks
            }
        }

        return tmpRes
    }

    /**
     * @throws CanNotDeleteFileException if track file not found
     * */
    override suspend fun deleteTrack(track: Track) {
        val tracksFlow = directoriesRepository.getDirectories().map { directoryEntities ->
            directoryEntities.map {
                DirectoryMapper().toDomain(
                    CommonDirectoryEntityMapper().toDomain(it as DirectoryEntity.CommonDirectoryEntity)
                )
            }.map { it }
        }

        tracksFlow.map { tracks ->
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
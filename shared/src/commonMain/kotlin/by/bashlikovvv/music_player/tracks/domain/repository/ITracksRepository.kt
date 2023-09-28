package by.bashlikovvv.music_player.tracks.domain.repository

import by.bashlikovvv.music_player.tracks.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface ITracksRepository {

    fun loadTracks(limit: Int, offset: Int): Flow<List<Track>>

    suspend fun deleteTrack(track: Track)

}
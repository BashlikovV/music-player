package by.bashlikovvv.music_player.tracks.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import by.bashlikovvv.music_player.core.data.TrackInfo
import by.bashlikovvv.music_player.core.mapper.Mapper
import by.bashlikovvv.music_player.tracks.domain.model.Directory
import by.bashlikovvv.music_player.tracks.domain.model.Track
import java.io.File

class DirectoryMapper : Mapper<Directory, Track> {
    override fun fromDomain(domain: Track): Directory {
        return Directory(
            id = domain.id,
            path = domain.trackFilePath + File.separator + domain.fileName
        )
    }

    override fun toDomain(entity: Directory): Track {
        val trackInfo = TrackInfo()
        trackInfo.setDataSource(entity.path)

        return Track(
            id = entity.id,
            trackFilePath = entity.path.substringBeforeLast(File.separator),
            fileName = entity.path.substringAfterLast(File.separator),
            duration = trackInfo.getDuration() ?: "",
            artist = trackInfo.getArtist() ?: entity.path.substringAfterLast("/").substringBeforeLast("-"),
            title = trackInfo.getTitle() ?: entity.path.substringAfterLast("-"),
            image = trackInfo.getImage()
        )
    }
}
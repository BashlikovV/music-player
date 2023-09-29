package by.bashlikovvv.music_player.tracks.data.mapper

import by.bashlikovvv.music_player.core.mapper.Mapper
import by.bashlikovvv.music_player.tracks.domain.model.Directory
import java.io.File

class FileToDirectoryMapper(private val id: Int) : Mapper<File, Directory> {
    override fun fromDomain(domain: Directory): File {
        return File(domain.path)
    }

    override fun toDomain(entity: File): Directory {
        return Directory(
            id = id,
            path = entity.path
        )
    }
}
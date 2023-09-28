package by.bashlikovvv.music_player.tracks.data.mapper

import by.bashlikovvv.music_player.core.mapper.Mapper
import by.bashlikovvv.music_player.tracks.data.model.DirectoryEntity
import by.bashlikovvv.music_player.tracks.domain.model.Directory

class CommonDirectoryEntityMapper : Mapper<DirectoryEntity.CommonDirectoryEntity, Directory> {
    override fun fromDomain(domain: Directory): DirectoryEntity.CommonDirectoryEntity {
        return DirectoryEntity.CommonDirectoryEntity(
            id = domain.id.toLong(),
            path = domain.path
        )
    }

    override fun toDomain(entity: DirectoryEntity.CommonDirectoryEntity): Directory {
        return Directory(
            id = entity.id.toInt(),
            path = entity.path
        )
    }
}
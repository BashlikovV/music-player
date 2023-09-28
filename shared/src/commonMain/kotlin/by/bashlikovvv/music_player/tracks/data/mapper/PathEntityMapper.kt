package by.bashlikovvv.music_player.tracks.data.mapper

import by.bashlikovvv.music_player.core.mapper.Mapper
import by.bashlikovvv.music_player.tracks.data.model.DirectoryEntity
import tracksDatabase.PathEntity

class PathEntityMapper : Mapper<PathEntity, DirectoryEntity.CommonDirectoryEntity> {
    override fun fromDomain(domain: DirectoryEntity.CommonDirectoryEntity): PathEntity {
        return PathEntity(
            id = domain.id,
            musicDirPath = domain.path
        )
    }

    override fun toDomain(entity: PathEntity): DirectoryEntity.CommonDirectoryEntity {
        return DirectoryEntity.CommonDirectoryEntity(
            id = entity.id,
            path = entity.musicDirPath
        )
    }
}
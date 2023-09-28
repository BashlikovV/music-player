package by.bashlikovvv.music_player.tracks.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed class DirectoryEntity {

    @Serializable
    data class CommonDirectoryEntity(
        val id: Long,
        val path: String
    ): DirectoryEntity()

}
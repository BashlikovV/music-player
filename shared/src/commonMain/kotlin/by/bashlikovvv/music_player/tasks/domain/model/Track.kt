package by.bashlikovvv.music_player.tasks.domain.model

data class Track(
    val path: String = "",
    val fileName: String = "",
    val isCurrent: Boolean = false
)
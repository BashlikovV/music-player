package by.bashlikovvv.music_player.tracks.domain.model

data class Track(
    val id: Int = 0,
    val trackFilePath: String = "",
    val fileName: String = "",
    val imageFilePath: String? = "",
    val isCurrent: Boolean = false
) {

    fun getTrackName() = fileName.substringAfter('-').substringBeforeLast(".")

    fun getMusician() = fileName.substringBefore('-')

}
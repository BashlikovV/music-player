package by.bashlikovvv.music_player

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
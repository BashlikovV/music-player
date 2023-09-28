package by.bashlikovvv.music_player.core.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import by.bashlikovvv.music_player.tracksDatabase.TracksDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(
            schema = TracksDatabase.Schema,
            context,
            DATABASE_NAME
        )
    }

    companion object {
        const val DATABASE_NAME = "tracks.db"
    }
}
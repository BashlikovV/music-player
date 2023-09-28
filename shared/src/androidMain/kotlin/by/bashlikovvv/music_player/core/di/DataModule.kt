package by.bashlikovvv.music_player.core.di

import app.cash.sqldelight.db.SqlDriver
import by.bashlikovvv.music_player.core.data.DatabaseDriverFactory
import by.bashlikovvv.music_player.tracksDatabase.TracksDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<SqlDriver> {
        DatabaseDriverFactory(context = androidContext()).create()
    }

    single<TracksDatabase> {
        TracksDatabase(driver = get())
    }

}
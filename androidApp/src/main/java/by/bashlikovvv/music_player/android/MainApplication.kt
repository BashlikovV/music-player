package by.bashlikovvv.music_player.android

import android.app.Application
import by.bashlikovvv.music_player.core.di.androidModule
import by.bashlikovvv.music_player.tracks.di.tasksModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger(Level.DEBUG)
            modules(androidModule() + tasksModule())
        }
    }

}
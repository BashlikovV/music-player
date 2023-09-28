package by.bashlikovvv.music_player.tracks.di

import by.bashlikovvv.music_player.tracks.data.repository.DirectoriesRepositoryImpl
import by.bashlikovvv.music_player.tracks.data.repository.TracksRepositoryImpl
import by.bashlikovvv.music_player.tracks.domain.repository.IDirectoriesRepository
import by.bashlikovvv.music_player.tracks.domain.repository.ITracksRepository
import org.koin.dsl.module

val dataModule = module {

    single<IDirectoriesRepository> {
        DirectoriesRepositoryImpl(db = get())
    }

    single<ITracksRepository> {
        TracksRepositoryImpl(directoriesRepository = get())
    }

}
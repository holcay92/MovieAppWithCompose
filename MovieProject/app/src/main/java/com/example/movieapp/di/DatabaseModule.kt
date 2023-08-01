package com.example.movieapp.di

import android.content.Context
import androidx.room.Room
import com.example.movieapp.room.Dao
import com.example.movieapp.room.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "movie_data_table",
        ).build()
    }

    @Provides
    fun provideDao(movieDatabase: MovieDatabase): Dao {
        return movieDatabase.dao()
    }
}

package com.example.movieapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun dao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null
        fun getInstance(context: Context): MovieDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "movie_data_table",
                    ).build()
                }
                return instance
            }
        }
    }
}

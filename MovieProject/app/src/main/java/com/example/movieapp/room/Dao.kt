package com.example.movieapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    @Insert
    suspend fun insertFavorite(movie: FavoriteMovie)

    @Delete
    suspend fun deleteFavorite(movie: FavoriteMovie): Int

    @Query("SELECT * FROM movie_data_table ORDER BY movie_id ASC")
    fun getAllItems(): LiveData<List<FavoriteMovie>>

    @Query("SELECT * FROM movie_data_table WHERE movie_id = :movieId")
    suspend fun getMovieById(movieId: Int): FavoriteMovie?
}

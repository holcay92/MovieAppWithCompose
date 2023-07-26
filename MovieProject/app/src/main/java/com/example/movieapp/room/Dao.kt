package com.example.movieapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface Dao {

    @Insert
    suspend fun InsertFavorite(movie: FavoriteMovie)

    @Delete
    suspend fun DeleteFavorite(movie: FavoriteMovie)
}

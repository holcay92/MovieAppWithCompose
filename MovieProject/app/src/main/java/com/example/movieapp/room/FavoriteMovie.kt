package com.example.movieapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_data_table")
data class FavoriteMovie(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "list_id")
    var listId: Int,
    @ColumnInfo(name = "movie_id")
    var id: Int?,
    @ColumnInfo(name = "movie_title")
    var title: String?,
    @ColumnInfo(name = "movie_poster_path")
    var posterPath: String?,
    @ColumnInfo(name = "average_vote")
    var averageVote: Double?,
)

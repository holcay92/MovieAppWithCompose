package com.example.movieapp.model.movieSearchResponse

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("isFavorite") var isFavorite: Boolean = false,
)

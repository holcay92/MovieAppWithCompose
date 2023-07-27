package com.example.movieapp.model.movieSearchResponse

import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    @SerializedName("results") val results: ArrayList<SearchResult>,
)

package com.example.movieapp.model.topRated

import com.google.gson.annotations.SerializedName

data class TopRated(
    val page: Int,
    @SerializedName("results") val results: List<ResultTopRated>,
    val total_pages: Int,
    val total_results: Int,
)

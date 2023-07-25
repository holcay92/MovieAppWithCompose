package com.example.movieapp.model.topRated

data class TopRated(
    val page: Int,
    val results: List<ResultTopRated>,
    val total_pages: Int,
    val total_results: Int
)
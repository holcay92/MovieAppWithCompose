package com.example.movieapp.model.popularMovie

data class PopularResponse(
    val page: Int,
    val results: List<ResultPopular>,
    val total_pages: Int,
    val total_results: Int,
)

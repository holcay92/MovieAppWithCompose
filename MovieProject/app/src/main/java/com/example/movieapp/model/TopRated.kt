package com.example.movieapp.model

data class TopRated(
    val page: Int,
    val results: List<ResultX>,
    val total_pages: Int,
    val total_results: Int
)
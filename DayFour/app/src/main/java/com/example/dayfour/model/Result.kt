package com.example.dayfour.model

data class Result(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<ResultX>
)
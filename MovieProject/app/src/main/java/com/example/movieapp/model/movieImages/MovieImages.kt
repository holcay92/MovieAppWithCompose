package com.example.movieapp.model.movieImages

import com.example.movieapp.model.movieImages.Backdrop
import com.example.movieapp.model.movieImages.Logo
import com.example.movieapp.model.movieImages.Poster

data class MovieImages(
    val backdrops: List<Backdrop>,
    val id: Int,
    val logos: List<Logo>,
    val posters: List<Poster>
)
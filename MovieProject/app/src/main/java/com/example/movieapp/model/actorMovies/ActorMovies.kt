package com.example.movieapp.model.actorMovies

import com.google.gson.annotations.SerializedName

data class ActorMovies(
    @SerializedName("cast") val cast: List<Any>?,
    @SerializedName("crew") val crew: List<ActorMoviesCrew>,
    @SerializedName("id") val id: Int
)
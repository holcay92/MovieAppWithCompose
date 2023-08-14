package com.example.movieapp.model.actorMovies

import com.google.gson.annotations.SerializedName

data class ActorMoviesCrew(

    @SerializedName("id") val id: Int,

    @SerializedName("poster_path") val posterPath: String,

)

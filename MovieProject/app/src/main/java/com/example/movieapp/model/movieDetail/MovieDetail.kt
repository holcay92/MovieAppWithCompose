package com.example.movieapp.model.movieDetail

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("adult") val adult: Boolean?,
    val backdrop_path: String,
    val belongs_to_collection: BelongsToCollection,
    @SerializedName("budget") val budget: Int?,
    @SerializedName("genres") val genres: List<Genre>?,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    @SerializedName("original_title") val original_title: String?,
    @SerializedName("overview") val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    @SerializedName("release_date") val release_date: String?,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    @SerializedName("title") val title: String?,
    val video: Boolean,
    @SerializedName("vote_average") val vote_average: Double?,
    val vote_count: Int,
)

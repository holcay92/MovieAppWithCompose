package com.example.movieapp.service

import com.example.movieapp.model.MovieDetail
import com.example.movieapp.model.MovieImages
import com.example.movieapp.model.MovieSearchResponse
import com.example.movieapp.model.PopularResponse
import com.example.movieapp.model.TopRated
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
    ): Call<MovieSearchResponse>

    @GET("movie/popular")
    fun getPopularMovies(): Call<PopularResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): Call<MovieDetail>

    @GET("movie/{movie_id}/images")
    fun getMovieImages(
        @Path("movie_id") movieId: Int,
    ): Call<MovieImages>

    @GET("movie/top_rated")
    fun getTopRatedMovies(): Call<TopRated>

    // @GET("movie/upcoming")
    // fun getUpComingMovies(@Query("page") page: Int): Call<MovieResponse>
}

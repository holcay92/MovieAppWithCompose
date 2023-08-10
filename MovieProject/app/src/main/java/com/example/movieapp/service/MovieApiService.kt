package com.example.movieapp.service

import com.example.movieapp.model.actor.Actor
import com.example.movieapp.model.credits.Credits
import com.example.movieapp.model.movieDetail.MovieDetail
import com.example.movieapp.model.movieImages.MovieImages
import com.example.movieapp.model.movieSearchResponse.MovieSearchResponse
import com.example.movieapp.model.popularMovie.PopularResponse
import com.example.movieapp.model.review.Review
import com.example.movieapp.model.topRated.TopRated
import com.example.movieapp.model.videos.Videos
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
    fun getPopularMovies(@Query("page") page: Int): Call<PopularResponse>

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

    @GET("movie/{movie_id}/reviews")
    fun getMovieReviews(
        @Path("movie_id") movieId: Int,
    ): Call<Review>

    // get movie videos todo: add to detail fragment
    @GET("movie/{movie_id}/videos")
    fun getMovieVideos(
        @Path("movie_id") movieId: Int,
    ): Call<Videos>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(@Path("movie_id") movieId: Int): Call<Credits?>?

    @GET("person/{person_id}")
    fun getActorDetails(
        @Path("person_id") personId: Int,
    ): Call<Actor>
}

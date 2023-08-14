package com.example.movieapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.model.movie.Movie
import com.example.movieapp.model.movie.MovieResult
import com.example.movieapp.room.MovieDatabase
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PopularMovieViewModel @Inject constructor(
    private val movieApiService: MovieApiService,
    private val movieDatabase: MovieDatabase,
) :
    ViewModel() {
    var popularMovieResponse = MutableLiveData<List<MovieResult>?>()
    var errorMessage = MutableLiveData<String>()
    init {
        fetchMovieList(1)
    }

    private fun fetchMovieList(page: Int) {
        val call = movieApiService.getPopularMovies(page)

        call.enqueue(
            object : Callback<Movie?> {
                override fun onResponse(
                    call: Call<Movie?>,
                    response: Response<Movie?>,
                ) {
                    if (response.isSuccessful) {
                        val results = response.body()?.results
                        results?.forEach { movie ->
                            viewModelScope.launch {
                                movie.isFavorite = movie.id?.let { isMovieInFavorites(it) } == true
                            }
                        }
                        popularMovieResponse.value = results
                    }
                }

                override fun onFailure(call: Call<Movie?>, t: Throwable) {
                    errorMessage.postValue("Failed to fetch movies. Please check your internet connection.")
                    popularMovieResponse.postValue(null)
                }
            },
        )
    }

    fun getNextPage(page: Int) {
        fetchMovieList(page)
    }

    private suspend fun isMovieInFavorites(movieId: Int): Boolean {
        return runBlocking { movieDatabase.dao().getMovieById(movieId) != null }
    }

    fun updateFavoriteResult() {
        viewModelScope.launch {
            val list = popularMovieResponse.value
            list?.forEach { movie ->
                movie.isFavorite = movie.id?.let { isMovieInFavorites(it) } == true
            }
            popularMovieResponse.postValue(list)
        }
    }
}

package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.model.popularMovie.PopularResponse
import com.example.movieapp.model.popularMovie.ResultPopular
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
    private val movieDatabase: MovieDatabase
) :
    ViewModel() {
    var popularMovieResponse = MutableLiveData<List<ResultPopular>?>()

    init {
        fetchMovieList(1)
    }

    private fun fetchMovieList(page: Int) {
        val call = movieApiService.getPopularMovies(page)

        call.enqueue(
            object : Callback<PopularResponse?> {
                override fun onResponse(
                    call: Call<PopularResponse?>,
                    response: Response<PopularResponse?>,
                ) {
                    if (response.isSuccessful) {
                        val results = response.body()?.results
                        results?.forEach { movie ->
                                viewModelScope.launch {
                                    movie.isFavorite = isMovieInFavorites(movie.id)
                                }
                        }
                        popularMovieResponse.value = results
                        Log.d("TAG_X", "PopularMovieViewModel onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<PopularResponse?>, t: Throwable) {
                    popularMovieResponse.postValue(null)
                }
            },
        )
    }

    fun getNextPage(page: Int) {
        fetchMovieList(page)
    }
    private suspend fun isMovieInFavorites(movieId: Int): Boolean {
       return runBlocking{ movieDatabase.dao().getMovieById(movieId) != null }
    }
}

package com.example.movieapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.model.topRated.ResultTopRated
import com.example.movieapp.model.topRated.TopRated
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
class TopRatedMovieViewModel @Inject constructor(
    private val movieApiService: MovieApiService,
    private val movieDatabase: MovieDatabase,
) :
    ViewModel() {
    var tRMovieResponse = MutableLiveData<List<ResultTopRated>?>()

    init {
        fetchMovieList()
    }

    private fun fetchMovieList() {
        val call = movieApiService.getTopRatedMovies()

        call.enqueue(
            object : Callback<TopRated> {
                override fun onResponse(
                    call: Call<TopRated?>,
                    response: Response<TopRated?>,
                ) {
                    if (response.isSuccessful) {
                        val results = response.body()?.results
                        results?.forEach { movie ->
                            viewModelScope.launch {
                                movie.isFavorite = isMovieInFavorites(movie.id)
                            }
                        }
                        tRMovieResponse.value = results
                    }
                }

                override fun onFailure(call: Call<TopRated?>, t: Throwable) {
                    tRMovieResponse.postValue(null)
                }
            },
        )
    }

    private suspend fun isMovieInFavorites(movieId: Int): Boolean {
        return runBlocking { movieDatabase.dao().getMovieById(movieId) != null }
    }
}

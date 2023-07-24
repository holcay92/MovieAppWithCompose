package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.PopularResponse
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieApiService: MovieApiService) :
    ViewModel() {
    var movieSearchResponse = MutableLiveData<PopularResponse?>()

    init {
        fetchMovieList()
    }

    private fun fetchMovieList() {
        val call = movieApiService.getPopularMovies()

        call.enqueue(
            object : Callback<PopularResponse?> {
                override fun onResponse(
                    call: Call<PopularResponse?>,
                    response: Response<PopularResponse?>,
                ) {
                    Log.d("TAG_X", "MovieViewModel onResponse: ${response.body()}")
                    if (response.isSuccessful) {
                        movieSearchResponse.value = response.body()
                    }
                }

                override fun onFailure(call: Call<PopularResponse?>, t: Throwable) {
                    movieSearchResponse.postValue(null)
                }
            },
        )
    }
}

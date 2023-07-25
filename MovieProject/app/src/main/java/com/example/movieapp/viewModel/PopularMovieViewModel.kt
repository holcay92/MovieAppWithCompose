package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.movieImages.MovieImages
import com.example.movieapp.model.popularMovie.PopularResponse
import com.example.movieapp.model.popularMovie.ResultPopular
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PopularMovieViewModel @Inject constructor(private val movieApiService: MovieApiService) :
    ViewModel() {
    var popularMovieResponse = MutableLiveData<List<ResultPopular>?>()
    var movieImages = MutableLiveData<MovieImages?>()

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
                    Log.d("TAG_X", "MovieViewModel onResponse: ${response.body()?.results}")
                    if (response.isSuccessful) {
                        popularMovieResponse.value = response.body()?.results
                    }
                }

                override fun onFailure(call: Call<PopularResponse?>, t: Throwable) {
                    popularMovieResponse.postValue(null)
                }
            },
        )
    }
}

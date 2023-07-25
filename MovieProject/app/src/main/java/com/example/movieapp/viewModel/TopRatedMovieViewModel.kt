package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.movieImages.MovieImages
import com.example.movieapp.model.topRated.ResultTopRated
import com.example.movieapp.model.topRated.TopRated
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TopRatedMovieViewModel @Inject constructor(private val movieApiService: MovieApiService) :
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
                    Log.d("TAG_X", "MovieViewModel onResponse: ${response.body()}")
                    if (response.isSuccessful) {
                        tRMovieResponse.value = response.body()?.results
                    }
                }

                override fun onFailure(call: Call<TopRated?>, t: Throwable) {
                    tRMovieResponse.postValue(null)
                }
            },
        )
    }
}

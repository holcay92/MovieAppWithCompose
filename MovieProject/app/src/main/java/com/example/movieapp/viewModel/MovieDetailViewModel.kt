package com.example.movieapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.movieDetail.MovieDetail
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val movieApiService: MovieApiService) :
    ViewModel() {
    var movieDetail = MutableLiveData<MovieDetail?>()

    fun fetchMovieDetail(id: Int) {
        val call = movieApiService.getMovieDetails(id)

        call.enqueue(
            object : Callback<MovieDetail?> {
                override fun onResponse(
                    call: Call<MovieDetail?>,
                    response: Response<MovieDetail?>,
                ) {
                    if (response.isSuccessful) {
                        movieDetail.value = response.body()
                    }
                }

                override fun onFailure(call: Call<MovieDetail?>, t: Throwable) {
                    movieDetail.postValue(null)
                }
            },
        )
    }
}

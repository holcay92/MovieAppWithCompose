package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.movieDetail.MovieDetail
import com.example.movieapp.model.videos.VideoResult
import com.example.movieapp.model.videos.Videos
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val movieApiService: MovieApiService) :
    ViewModel() {
    var movieDetail = MutableLiveData<MovieDetail?>()
    var movieVideos = MutableLiveData<List<VideoResult>?>()

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

    fun fetchMovieVideos(id: Int) {
        val call = movieApiService.getMovieVideos(id)

        call.enqueue(
            object : Callback<Videos?> {
                override fun onResponse(
                    call: Call<Videos?>,
                    response: Response<Videos?>,
                ) {
                    if (response.isSuccessful) {
                        movieVideos.value = response.body()?.results
                        Log.d("MovieDetailViewModel", "Videos onResponse: ${response.body()?.results}")
                    }
                }

                override fun onFailure(call: Call<Videos?>, t: Throwable) {
                    movieVideos.postValue(null)
                }
            },
        )
    }
}

package com.example.movieapp.viewModel

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
    var errorMessageMovieDetail = MutableLiveData<String>()
    var errorMessageMovieVideos = MutableLiveData<String>()
    val loadingState = MutableLiveData<Boolean>()

    fun fetchMovieDetail(id: Int) {
        loadingState.value = true
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
                    loadingState.value = false
                }

                override fun onFailure(call: Call<MovieDetail?>, t: Throwable) {
                    errorMessageMovieDetail.postValue("Movie Detail Fetch Error") // todo add translation
                    movieDetail.postValue(null)
                    loadingState.value = false
                }
            },
        )
    }

    fun fetchMovieVideos(id: Int) {
        loadingState.value = true
        val call = movieApiService.getMovieVideos(id)

        call.enqueue(
            object : Callback<Videos?> {
                override fun onResponse(
                    call: Call<Videos?>,
                    response: Response<Videos?>,
                ) {
                    if (response.isSuccessful) {
                        movieVideos.value = response.body()?.results
                    }
                    loadingState.value = false
                }

                override fun onFailure(call: Call<Videos?>, t: Throwable) {
                    errorMessageMovieVideos.postValue("Movie Videos Fetch Error") // todo add translation
                    movieVideos.postValue(null)
                    loadingState.value = false
                }
            },

        )
    }
}

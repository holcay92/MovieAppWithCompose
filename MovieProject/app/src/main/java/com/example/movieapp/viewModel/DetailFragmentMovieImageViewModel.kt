package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.movieImages.MovieImages
import com.example.movieapp.model.movieImages.Poster
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
@HiltViewModel
class DetailFragmentMovieImageViewModel @Inject constructor(private val movieApiService: MovieApiService) :
    ViewModel() {

    var imageResponse = MutableLiveData<List<Poster>?>()

    fun fetchMovieImageList(id: Int) {
        val call = movieApiService.getMovieImages(id)
        call.enqueue(
            object : Callback<MovieImages> {
                override fun onResponse(
                    call: Call<MovieImages?>,
                    response: Response<MovieImages?>,
                ) {
                    if (response.isSuccessful) {
                        imageResponse.value = response.body()?.posters
                    }
                }

                override fun onFailure(call: Call<MovieImages?>, t: Throwable) {
                    imageResponse.postValue(null)
                }
            },
        )
    }
}

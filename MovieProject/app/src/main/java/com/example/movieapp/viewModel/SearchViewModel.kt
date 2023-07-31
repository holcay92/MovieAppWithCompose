package com.example.movieapp.viewModel

import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.movieSearchResponse.MovieSearchResponse
import com.example.movieapp.model.movieSearchResponse.SearchResult
import com.example.movieapp.room.MovieDatabase
import com.example.movieapp.service.MovieApiService
import com.example.movieapp.view.adapters.SearchListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieApiService: MovieApiService,
    private val movieDatabase: MovieDatabase
) :
    ViewModel() {
    val searchList = MutableLiveData<List<SearchResult>?>()

    fun searchMovies(query: String){
        val call = movieApiService.searchMovies(query)
        Log.d("TAG_X SearchViewModel", "searchMovies: $query")
        call.enqueue(object : Callback<MovieSearchResponse?> {
            override fun onResponse(
                call: Call<MovieSearchResponse?>,
                response: Response<MovieSearchResponse?>,
            ) {
                Log.d(
                    "TAG_X SearchViewModel",
                    "Adapter onResponse response.body()?.results: ${response.body()?.results}"
                )
                if (response.isSuccessful) {
                    val results = response.body()?.results
                    if (results != null) {
                        results.forEach { movie ->
                            runBlocking {
                                withContext(coroutineContext) {
                                    movie.isFavorite = isMovieInFavorites(movie.id!!)
                                }
                            }
                        }
                        searchList.value = results
                    }
                }
            }

            override fun onFailure(call: Call<MovieSearchResponse?>, t: Throwable) {
                Log.d("TAG_X SearchViewModel", "onFailure: ${t.message}")
            }
        })
    }

    private suspend fun isMovieInFavorites(movieId: Int): Boolean {
        return runBlocking{ movieDatabase.dao().getMovieById(movieId) != null }
    }
}

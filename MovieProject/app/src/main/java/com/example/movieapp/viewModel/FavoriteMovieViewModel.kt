package com.example.movieapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.room.MovieDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(context: Application) : ViewModel() {
    private val dao = MovieDatabase.getInstance(context).dao()

    var favMovieList: LiveData<List<FavoriteMovie>> = dao.getAllItems()

    fun actionFavButton(movie: FavoriteMovie) = viewModelScope.launch {
        val favoriteMovies = favMovieList.value

        if (favoriteMovies?.any { it.id == movie.id } == true) {
            val deleteMovie = favoriteMovies.find { it.id == movie.id }
            dao.deleteFavorite(deleteMovie!!)
        } else {
            dao.insertFavorite(movie)
        }
    }
}

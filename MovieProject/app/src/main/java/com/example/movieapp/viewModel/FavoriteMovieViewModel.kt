package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.room.Dao
import com.example.movieapp.room.FavoriteMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(private val dao: Dao) : ViewModel() {

    var favMovieList: LiveData<List<FavoriteMovie>> = dao.getAllItems()

    fun actionFavButton(movie: FavoriteMovie) = viewModelScope.launch {
        val favoriteMovies = favMovieList.value.orEmpty()
        Log.d("TAGX", "actionFavButton list: $favoriteMovies")
        Log.d("TAGX", "actionFavButton movie parameter: $movie")

        if (isFavorite(movie)) {
            Log.d("TAGX", "actionFavButton movie delete: $movie")
            dao.deleteFavorite(movie)
        } else {
            Log.d("TAGX", "actionFavButton movie insert: $movie")
            dao.insertFavorite(movie)
        }
    }

    private fun isFavorite(movie: FavoriteMovie): Boolean {
        val favoriteMovies = favMovieList.value.orEmpty()
        return favoriteMovies.any { it.id == movie.id }
    }
}

package com.example.movieapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentDetailBinding
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.view.adapters.MovieImageAdapter
import com.example.movieapp.viewModel.DetailFragmentMovieImageViewModel
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import com.example.movieapp.viewModel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var bindingDetail: FragmentDetailBinding
    private val viewModelForImage by viewModels<DetailFragmentMovieImageViewModel>()
    private val viewModelForDetail by viewModels<MovieDetailViewModel>()
    private val viewModelForFavorite by viewModels<FavoriteMovieViewModel>()
    private lateinit var adapter: MovieImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindingDetail = FragmentDetailBinding.inflate(inflater, container, false)
        return bindingDetail.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = DetailFragmentArgs.fromBundle(requireArguments()).id
        Log.d("TAG_X", "DetailFragment onViewCreated: $id")
        adapter = MovieImageAdapter()
        bindingDetail.viewPager.adapter = adapter
        viewModelForImage.fetchMovieImageList(id)
        viewModelForImage.imageResponse.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
        viewModelForDetail.fetchMovieDetail(id)
        viewModelForDetail.movieDetail.observe(viewLifecycleOwner) {
            updateUI()
        }

        viewModelForFavorite.favMovieList.observe(viewLifecycleOwner) { favoriteMovies ->
            val isFav = favoriteMovies.any { it.id == id }
            updateFavButtonState(isFav)
            bindingDetail.favButton.setOnClickListener {
                val favMovie = FavoriteMovie(0, id)
                viewModelForFavorite.actionFavButton(favMovie)
            }
        }
    }

    private fun updateUI() {
        val response = viewModelForDetail.movieDetail.value
        bindingDetail.apply {
            response?.let {
                movieTitle.text = it.title
                bindingDetail.movieReleaseDate.text = it.release_date
                bindingDetail.movieOverview.text = it.overview
                bindingDetail.movieVote.text = it.vote_average.toString()
                bindingDetail.movieBudget.text = it.budget.toString()
                bindingDetail.movieAdult.text = if (it.adult!!) "Yes" else "No"
                bindingDetail.movieOriginalTitle.text = it.original_title
            }
        }
    }

    private fun updateFavButtonState(isFav: Boolean) {
        val drawableResId =
            if (isFav) R.drawable.add_fav_filled_icon else R.drawable.add_fav_empty_icon
        bindingDetail.favButton.setBackgroundResource(drawableResId)
    }
}

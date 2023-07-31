package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
        // handle back button
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback,
        )
        //get id from bundle
        val id = DetailFragmentArgs.fromBundle(requireArguments()).id
        //set up viewpager
        adapter = MovieImageAdapter()
        bindingDetail.viewPager.adapter = adapter

        //Todo: check if it is best practice to use 2 observations
        viewModelForImage.fetchMovieImageList(id)
        viewModelForImage.imageResponse.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
        viewModelForDetail.fetchMovieDetail(id)
        viewModelForDetail.movieDetail.observe(viewLifecycleOwner) {
            updateUI()
        }
        //show reviews
        bindingDetail.showReviews.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToDetailReviewFragment(id)
            findNavController().navigate(action)
        }

        viewModelForFavorite.favMovieList.observe(viewLifecycleOwner) { favoriteMovies ->
            val isFav = favoriteMovies.any { it.id == id }
            updateFavButtonState(isFav)
            bindingDetail.favButton.setOnClickListener {
                val movie = viewModelForDetail.movieDetail.value
                val favMovie = FavoriteMovie(0, id, movie?.title, movie?.poster_path)
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
                bindingDetail.movieVoteCount.text = it.vote_count.toString()
                val toolbar = activity as AppCompatActivity
                toolbar.supportActionBar?.title = it.title
            }
        }
    }

    private fun updateFavButtonState(isFav: Boolean) {
        val drawableResId =
            if (isFav) R.drawable.add_fav_filled_icon else R.drawable.add_fav_empty_icon
        bindingDetail.favButton.setBackgroundResource(drawableResId)
    }
}

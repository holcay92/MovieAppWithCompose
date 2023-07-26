package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.movieapp.databinding.FragmentDetailBinding
import com.example.movieapp.view.adapters.MovieImageAdapter
import com.example.movieapp.viewModel.DetailFragmentMovieImageViewModel
import com.example.movieapp.viewModel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModelForImage by viewModels<DetailFragmentMovieImageViewModel>()
    private val viewModelForDetail by viewModels<MovieDetailViewModel>()
    private lateinit var adapter: MovieImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = DetailFragmentArgs.fromBundle(requireArguments()).id
        adapter = MovieImageAdapter()
        binding.viewPager.adapter = adapter
        viewModelForImage.fetchMovieImageList(id)
        viewModelForImage.imageResponse.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
        viewModelForDetail.fetchMovieDetail(id)
        viewModelForDetail.movieDetail.observe(viewLifecycleOwner) {
            updateUI()
        }
    }

    private fun updateUI() {
        val response = viewModelForDetail.movieDetail.value
        binding.apply {
            response?.let {
                movieTitle.text = it.title
                binding.movieReleaseDate.text = it.release_date
                binding.movieOverview.text = it.overview
                binding.movieVote.text = it.vote_average.toString()
                binding.movieBudget.text = it.budget.toString()
                binding.movieAdult.text = if (it.adult!!) "Yes" else "No"
                binding.movieOriginalTitle.text = it.original_title
            }
        }
    }
}

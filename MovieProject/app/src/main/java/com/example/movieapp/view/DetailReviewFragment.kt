package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentDetailReviewBinding
import com.example.movieapp.view.adapters.DetailReviewAdapter
import com.example.movieapp.viewModel.DetailReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailReviewFragment : BaseFragment() {
    private lateinit var binding: FragmentDetailReviewBinding
    private lateinit var reviewAdapter: DetailReviewAdapter
    private val detailReviewViewModel by viewModels<DetailReviewViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        setupReviewObservers()
    }

    private fun setupToolbar() {
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.setTitle(R.string.show_reviews)
    }

    private fun setupRecyclerView() {
        reviewAdapter = DetailReviewAdapter()
        binding.rvDetailReview.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDetailReview.adapter = reviewAdapter
    }

    private fun setupReviewObservers() {
        val movieId = DetailReviewFragmentArgs.fromBundle(requireArguments()).movieId

        showProgressDialog()
        detailReviewViewModel.getReview(movieId)

        detailReviewViewModel.reviewList.observe(viewLifecycleOwner) { reviews ->
            hideProgressDialog()

            if (reviews.isNullOrEmpty()) {
                binding.tvDetailReviewNoReviewsFound.visibility = View.VISIBLE
            } else {
                binding.tvDetailReviewNoReviewsFound.visibility = View.GONE
                reviewAdapter.updateList(reviews)
            }
        }

        detailReviewViewModel.errorMessageMovieReview.observe(viewLifecycleOwner) { errorMessage ->
            hideProgressDialog()
            showErrorDialog(errorMessage)
        }
    }
}

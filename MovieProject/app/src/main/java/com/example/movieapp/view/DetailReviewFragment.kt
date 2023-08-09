package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentDetailReviewBinding
import com.example.movieapp.view.adapters.DetailReviewAdapter
import com.example.movieapp.viewModel.DetailReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailReviewFragment : Fragment() {
    private lateinit var fragmentDetailReviewBinding: FragmentDetailReviewBinding
    private lateinit var detailReviewAdapter: DetailReviewAdapter
    private val detailReviewViewModel by viewModels<DetailReviewViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentDetailReviewBinding = FragmentDetailReviewBinding.inflate(inflater, container, false)
        return fragmentDetailReviewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = DetailReviewFragmentArgs.fromBundle(
            requireArguments(),
        ).movieId
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.setTitle(R.string.show_reviews)

        detailReviewAdapter = DetailReviewAdapter()
        fragmentDetailReviewBinding.rvDetailReview.layoutManager = LinearLayoutManager(requireContext())
        fragmentDetailReviewBinding.rvDetailReview.adapter = detailReviewAdapter
        // linear layout manager is default for recyclerview
        detailReviewViewModel.getReview(id)
        detailReviewViewModel.reviewList.observe(viewLifecycleOwner) {
            if (it != null) {
                detailReviewAdapter.updateList(it)
            }
            if (it?.isEmpty() == true) {
                fragmentDetailReviewBinding.tvDetailReviewNoReviewsFound.visibility = View.VISIBLE
            } else {
                fragmentDetailReviewBinding.tvDetailReviewNoReviewsFound.visibility = View.GONE
            }
        }
    }
}

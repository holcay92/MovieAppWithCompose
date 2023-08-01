package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.FragmentDetailReviewBinding
import com.example.movieapp.view.adapters.DetailReviewAdapter
import com.example.movieapp.viewModel.DetailReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailReviewFragment : Fragment() {
    private lateinit var binding: FragmentDetailReviewBinding
    private lateinit var adapter: DetailReviewAdapter
    private val viewModel by viewModels<DetailReviewViewModel>()

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
        val id = DetailReviewFragmentArgs.fromBundle(
            requireArguments(),
        ).movieId
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.title = "Reviews"

        adapter = DetailReviewAdapter()
        binding.rvDetailReview.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDetailReview.adapter = adapter
        // linear layout manager is default for recyclerview
        viewModel.getReview(id)
        viewModel.reviewList.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }
}

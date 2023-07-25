package com.example.movieapp.view

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMainBinding
import com.example.movieapp.model.popularMovie.ResultPopular
import com.example.movieapp.model.topRated.ResultTopRated
import com.example.movieapp.viewModel.PopularMovieViewModel
import com.example.movieapp.viewModel.TopRatedMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModelPopular by viewModels<PopularMovieViewModel>()
    private val viewModelTR by viewModels<TopRatedMovieViewModel>()
    private lateinit var progressDialog: Dialog
    private lateinit var adapterPopular: PopularMovieAdapter
    private lateinit var adapterTR: TopRatedMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        // showProgressDialog()
        Log.d("TAG_X", "Main Fragment onCreateView in the fragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPopularMovies.layoutManager =
            GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false)
        adapterPopular = PopularMovieAdapter(
            object : PopularMovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: ResultPopular) {
                    val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
                    findNavController().navigate(action)
                    Log.d("TAG_X", "Main Fragment onItemClick in the fragment: $movie")
                }
            },
        )
        binding.rvPopularMovies.adapter = adapterPopular

        binding.rvTopRatedMovies.layoutManager =
            GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false)
        adapterTR = TopRatedMovieAdapter(
            object : TopRatedMovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: ResultTopRated) {
                    val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
                    findNavController().navigate(action)
                    Log.d("TAG_X", "Main Fragment onItemClick in the fragment: $movie")
                }
            },
        )
        binding.rvTopRatedMovies.adapter = adapterTR

        viewModelTR.tRMovieResponse.observe(viewLifecycleOwner) {
            adapterTR.updateList(it)
            Log.d("TAG_X", "Main Fragment updateList in the fragment: $it")
        }

        viewModelPopular.popularMovieResponse.observe(viewLifecycleOwner) {
            adapterPopular.updateList(it)
            Log.d("TAG_X", "Main Fragment updateList in the fragment: $it")
        }
    }

    private fun showProgressDialog() {
        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        val handler = Handler()
        val progressBarDelay = 500L
        handler.postDelayed({
            progressDialog.dismiss()
        }, progressBarDelay)
    }
}

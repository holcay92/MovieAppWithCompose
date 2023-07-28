package com.example.movieapp.view.homePage

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.Constants
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMainBinding
import com.example.movieapp.model.popularMovie.ResultPopular
import com.example.movieapp.model.topRated.ResultTopRated
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.view.adapters.PopularMovieAdapter
import com.example.movieapp.view.adapters.TopRatedMovieAdapter
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import com.example.movieapp.viewModel.PopularMovieViewModel
import com.example.movieapp.viewModel.TopRatedMovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModelPopular by viewModels<PopularMovieViewModel>()
    private val viewModelTR by viewModels<TopRatedMovieViewModel>()
    private lateinit var progressDialog: Dialog
    private lateinit var adapterPopular: PopularMovieAdapter
    private lateinit var adapterTR: TopRatedMovieAdapter
    private var viewType = false
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        showProgressDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.title = "Movie App"

        binding.gridBtn.setOnClickListener {
            viewType = !viewType
            switchRecyclerViewLayout()
        }
        binding.rvPopularMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvPopularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {

                    page++
                    //showProgressDialog()
                    Log.d("TAG_X", "Main Fragment page: $page")
                   // Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                    viewModelPopular.getNextPage(page)
                    //hideProgressDialog()
                }

            }

        })
        adapterPopular = PopularMovieAdapter(
            object : PopularMovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: ResultPopular) {
                    val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                        Constants.POPULAR,
                        movie.id,

                        )
                    findNavController().navigate(action)
                }
            },
        )
        binding.rvPopularMovies.adapter = adapterPopular

        binding.rvTopRatedMovies.layoutManager =
            GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false)
        adapterTR = TopRatedMovieAdapter(
            object : TopRatedMovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: ResultTopRated) {
                    val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                        Constants.TOP_RATED,
                        movie.id,
                    )
                    findNavController().navigate(action)
                }
            },
        )
        binding.rvTopRatedMovies.adapter = adapterTR

        runBlocking {
            viewModelTR.tRMovieResponse.observe(viewLifecycleOwner) {
                adapterTR.updateList(it)
                // Log.d("TAG_X", "Main Fragment updateList in the fragment: $it")
            }

            viewModelPopular.popularMovieResponse.observe(viewLifecycleOwner) {
                adapterPopular.updateList(it)
                // Log.d("TAG_X", "Main Fragment updateList in the fragment: $it")
            }
        }
        hideProgressDialog()

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

    private fun switchRecyclerViewLayout() {
        if (viewType) {
            binding.rvPopularMovies.layoutManager =
                GridLayoutManager(requireContext(), 2)
            binding.gridBtn.setImageResource(R.drawable.list_view)
        } else {
            binding.rvPopularMovies.layoutManager = LinearLayoutManager(requireContext())
            binding.gridBtn.setImageResource(R.drawable.grid_view)
        }
        binding.rvPopularMovies.adapter?.notifyDataSetChanged()
    }

}

package com.example.movieapp.view.homePage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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
class MainFragment : Fragment(), PopularMovieAdapter.OnFavoriteStatusChangeListener {
    private lateinit var fragmentMainBinding: FragmentMainBinding
    private val popularMovieViewModel by viewModels<PopularMovieViewModel>()
    private val topRatedMovieViewModel by viewModels<TopRatedMovieViewModel>()
    private val favoriteMovieViewModel by viewModels<FavoriteMovieViewModel>()
    private lateinit var progressDialog: Dialog
    private lateinit var popularMovieAdapter: PopularMovieAdapter
    private lateinit var topRatedMovieAdapter: TopRatedMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        showProgressDialog()
        return fragmentMainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setup user interface
        setupViews()
        setupBackPressedCallback()
        setupRecyclerViews()
        setupGridButtonClickListener()

        fragmentMainBinding.gridBtn.setOnClickListener {
            viewType = !viewType
            switchRecyclerViewLayout()
        }

        fetchData()
        favoriteMovieViewModel.favMovieList.observe(viewLifecycleOwner) {
        }
    }

    private fun fetchData() {
        runBlocking {
            topRatedMovieViewModel.tRMovieResponse.observe(viewLifecycleOwner) {
                topRatedMovieAdapter.updateList(it)
            }

            popularMovieViewModel.popularMovieResponse.observe(viewLifecycleOwner) {
                popularMovieAdapter.updateList(it)
            }
        }
        hideProgressDialog()
    }

    private fun setupBackPressedCallback() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                alertDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback,
        )
    }

    private fun setupViews() {
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.title = ""
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        SPAN_COUNT = if (isLandscape) 5 else 2
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupGridButtonClickListener() {
        fragmentMainBinding.gridBtn.setOnClickListener {
            viewType = !viewType
            switchRecyclerViewLayout()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun showProgressDialog() {
        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun switchRecyclerViewLayout() {
        fragmentMainBinding.rvPopularMovies.layoutManager =
            if (viewType) {
                GridLayoutManager(requireContext(), SPAN_COUNT)
            } else {
                LinearLayoutManager(requireContext())
            }
        popularMovieAdapter.setViewType(
            if (viewType) {
                PopularMovieAdapter.ViewType.GRID
            } else {
                PopularMovieAdapter.ViewType.LIST
            },
        )
        fragmentMainBinding.gridBtn.setImageResource(
            if (viewType) {
                R.drawable.list_view
            } else {
                R.drawable.grid_view
            },
        )
        fragmentMainBinding.rvPopularMovies.adapter?.notifyDataSetChanged()
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alert")
        builder.setMessage("Do you want to exit?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { _, _ ->
            requireActivity().finish() // Exit the app
        }
        builder.setNegativeButton("No") { _, _ ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setupRecyclerViews() {
        fragmentMainBinding.rvPopularMovies.layoutManager = LinearLayoutManager(requireContext())
        popularMovieAdapter = PopularMovieAdapter(
            object : PopularMovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: ResultPopular) {
                    val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                        Constants.POPULAR,
                        movie.id,
                    )
                    findNavController().navigate(action)
                }
            },
            this,
        )
        fragmentMainBinding.rvPopularMovies.adapter = popularMovieAdapter

        fragmentMainBinding.rvPopularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    page++
                    popularMovieViewModel.getNextPage(page)
                }
            }
        })

        fragmentMainBinding.rvTopRatedMovies?.layoutManager =
            GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false)
        topRatedMovieAdapter = TopRatedMovieAdapter(object : TopRatedMovieAdapter.OnItemClickListener {
            override fun onItemClick(movie: ResultTopRated) {
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                    Constants.TOP_RATED,
                    movie.id,
                )
                findNavController().navigate(action)
            }
        })
        fragmentMainBinding.rvTopRatedMovies?.adapter = topRatedMovieAdapter
    }

    companion object {
        private var SPAN_COUNT = 2
        private var viewType = false
        private var page = 1
    }

    override fun onFavoriteStatusChanged(movie: ResultPopular) {
        favoriteMovieViewModel.favMovieList.observe(viewLifecycleOwner) { favoriteMovies ->
            popularMovieAdapter.updateFavoriteStatus(favoriteMovies)
        }
        favoriteMovieViewModel.actionFavButton(
            FavoriteMovie(
                0,
                movie.id,
                movie.title,
                movie.poster_path,
            ),
        )
    }
}

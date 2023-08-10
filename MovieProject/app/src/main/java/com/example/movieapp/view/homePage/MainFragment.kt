package com.example.movieapp.view.homePage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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
import com.example.movieapp.view.BaseFragment
import com.example.movieapp.view.adapters.PopularMovieAdapter
import com.example.movieapp.view.adapters.TopRatedMovieAdapter
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import com.example.movieapp.viewModel.PopularMovieViewModel
import com.example.movieapp.viewModel.TopRatedMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment :
    BaseFragment(),
    PopularMovieAdapter.OnFavoriteStatusChangeListener,
    TopRatedMovieAdapter.OnFavoriteStatusChangeListener {
    private lateinit var fragmentMainBinding: FragmentMainBinding
    private val popularMovieViewModel by viewModels<PopularMovieViewModel>()
    private val topRatedMovieViewModel by viewModels<TopRatedMovieViewModel>()
    private val favoriteMovieViewModel by viewModels<FavoriteMovieViewModel>()

    private lateinit var popularMovieAdapter: PopularMovieAdapter
    private lateinit var topRatedMovieAdapter: TopRatedMovieAdapter
    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        showProgressDialog()
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
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

        favoriteMovieViewModel.favMovieList.observe(viewLifecycleOwner) {
        }
    }

    private fun fetchData() {
        topRatedMovieViewModel.tRMovieResponse.observe(viewLifecycleOwner) {
            topRatedMovieAdapter.updateList(it)
            hideProgressDialog()
        }

        popularMovieViewModel.popularMovieResponse.observe(viewLifecycleOwner) {
            popularMovieAdapter.updateList(it)
            hideProgressDialog()
            isLoading = false
        }
        popularMovieViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Log.d("TAGX", "fetchData: $message")
                showErrorDialog(message.toString()) // show error dialog for fetching popular movies
            }
        }
        topRatedMovieViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                showErrorDialog(message.toString()) // show error dialog for fetching top rated movies
            }
        }
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
        SPAN_COUNT = if (isLandscape) 5 else 3
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
        popularMovieViewModel.updateFavoriteResult()
        topRatedMovieViewModel.updateFavoriteResult()
        fetchData()
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
                R.drawable.list_view_icon
            } else {
                R.drawable.grid_view_icon
            },
        )
        fragmentMainBinding.rvPopularMovies.adapter?.notifyDataSetChanged()
    }

    private fun setupRecyclerViews() {
        fragmentMainBinding.rvPopularMovies.layoutManager = LinearLayoutManager(requireContext())
        popularMovieAdapter = PopularMovieAdapter(
            object : PopularMovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: ResultPopular) {
                    val action = movie.id?.let {
                        MainFragmentDirections.actionMainFragmentToDetailFragment(
                            Constants.POPULAR,
                            it,
                        )
                    }
                    if (action != null) {
                        findNavController().navigate(action)
                    }
                }
            },
            this,
        )
        fragmentMainBinding.rvPopularMovies.adapter = popularMovieAdapter

        fragmentMainBinding.rvPopularMovies.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    isLoading = true // to prevent multiple calls
                    page++
                    popularMovieViewModel.getNextPage(page)
                }
            }
        })

        fragmentMainBinding.rvTopRatedMovies?.layoutManager =
            GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false)
        topRatedMovieAdapter =
            TopRatedMovieAdapter(
                object : TopRatedMovieAdapter.OnItemClickListener {
                    override fun onItemClick(movie: ResultTopRated) {
                        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                            Constants.TOP_RATED,
                            movie.id,
                        )
                        findNavController().navigate(action)
                    }
                },
                this,
            )
        fragmentMainBinding.rvTopRatedMovies?.adapter = topRatedMovieAdapter
    }

    companion object {
        private var SPAN_COUNT = 2
        private var viewType = false
        private var page = 1
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.warning)
        builder.setMessage(R.string.quite_from_app__message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            requireActivity().finish() // Exit the app
        }
        builder.setNegativeButton(R.string.no) { _, _ ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
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
                movie.posterPath,
                movie.voteAverage,
            ),
        )
    }

    override fun onFavoriteStatusChanged(movie: ResultTopRated) {
        favoriteMovieViewModel.favMovieList.observe(viewLifecycleOwner) { favoriteMovies ->
            topRatedMovieAdapter.updateFavoriteStatus(favoriteMovies)
        }
        favoriteMovieViewModel.actionFavButton(
            FavoriteMovie(
                0,
                movie.id,
                movie.title,
                movie.poster_path,
                movie.voteAverage,
            ),
        )
    }
}

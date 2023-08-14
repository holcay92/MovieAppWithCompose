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
import com.example.movieapp.model.movie.MovieResult
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.view.BaseFragment
import com.example.movieapp.view.adapters.NowPlayingMovieAdapter
import com.example.movieapp.view.adapters.PopularMovieAdapter
import com.example.movieapp.view.adapters.TopRatedMovieAdapter
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import com.example.movieapp.viewModel.NowPlayingMovieViewModel
import com.example.movieapp.viewModel.PopularMovieViewModel
import com.example.movieapp.viewModel.TopRatedMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment :
    BaseFragment(),
    PopularMovieAdapter.OnFavoriteStatusChangeListener,
    TopRatedMovieAdapter.OnFavoriteStatusChangeListener,
    NowPlayingMovieAdapter.OnFavoriteStatusChangeListener {
    private lateinit var fragmentMainBinding: FragmentMainBinding
    private val popularMovieViewModel by viewModels<PopularMovieViewModel>()
    private val topRatedMovieViewModel by viewModels<TopRatedMovieViewModel>()
    private val favoriteMovieViewModel by viewModels<FavoriteMovieViewModel>()
    private val nowPlayingMovieViewModel by viewModels<NowPlayingMovieViewModel>()

    private lateinit var popularMovieAdapter: PopularMovieAdapter
    private lateinit var topRatedMovieAdapter: TopRatedMovieAdapter
    private lateinit var nowPlayingMovieAdapter: NowPlayingMovieAdapter
    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        showProgressDialog()
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)

       /* val topRatedMoviesLayout = fragmentMainBinding.llTopRatedMovies

        val scrollView = fragmentMainBinding.scrollView
        scrollView.viewTreeObserver?.addOnScrollChangedListener {
            val scrollY = scrollView.scrollY
            if (scrollY > 20) {
                // Scroll has passed the threshold, hide the top rated movies section with animation
                topRatedMoviesLayout?.animate()?.alpha(0f)?.setDuration(300)?.start()
                if (scrollY > 50) {
                    topRatedMoviesLayout?.visibility = View.GONE
                }
            } else {
                // Scroll is back above the threshold, show the top rated movies section with animation
                topRatedMoviesLayout?.animate()?.alpha(1f)?.setDuration(300)?.start()
                topRatedMoviesLayout?.visibility = View.VISIBLE
            }
        }*/
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

        nowPlayingMovieViewModel.nowPlayingMovies.observe(viewLifecycleOwner) {
            nowPlayingMovieAdapter.updateList(it)
            hideProgressDialog()
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

        nowPlayingMovieViewModel.errorMessageNowPlayingMovies.observe(viewLifecycleOwner) { message ->
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
        val isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        SPAN_COUNT = if (isLandscape) SPAN_COUNT_5 else SPAN_COUNT_3
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
        fragmentMainBinding.rvPopularMovies.layoutManager =
            LinearLayoutManager(requireContext())
        popularMovieAdapter = PopularMovieAdapter(
            object : PopularMovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: MovieResult) {
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
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        topRatedMovieAdapter =
            TopRatedMovieAdapter(
                object : TopRatedMovieAdapter.OnItemClickListener {
                    override fun onItemClick(movie: MovieResult) {
                        val action = movie.id?.let {
                            MainFragmentDirections.actionMainFragmentToDetailFragment(
                                Constants.TOP_RATED,
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
        fragmentMainBinding.rvTopRatedMovies?.adapter = topRatedMovieAdapter

        fragmentMainBinding.rvNowPlayingMovies?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        nowPlayingMovieAdapter = NowPlayingMovieAdapter(
            object : NowPlayingMovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: MovieResult) {
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
        fragmentMainBinding.rvNowPlayingMovies?.adapter = nowPlayingMovieAdapter
    }

    companion object {
        private var SPAN_COUNT = 2
        private var SPAN_COUNT_1 = 1
        private var SPAN_COUNT_3 = 3
        private var SPAN_COUNT_5 = 5
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

    override fun onFavoriteStatusChanged(movie: MovieResult) {
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

    override fun onFavoriteStatusChanged1(movie: MovieResult) {
        favoriteMovieViewModel.favMovieList.observe(viewLifecycleOwner) { favoriteMovies ->
            topRatedMovieAdapter.updateFavoriteStatus(favoriteMovies)
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

    override fun onFavoriteStatusChanged3(movie: MovieResult) {
        favoriteMovieViewModel.favMovieList.observe(viewLifecycleOwner) { favoriteMovies ->
            nowPlayingMovieAdapter.updateFavoriteStatus(favoriteMovies)
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
}

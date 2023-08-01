package com.example.movieapp.view.homePage

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
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
import com.example.movieapp.view.adapters.PopularMovieAdapter
import com.example.movieapp.view.adapters.TopRatedMovieAdapter
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
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setup user interface
        setupViews()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                alertDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback,
        )

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
                    // showProgressDialog()
                    Log.d("TAG_X", "Main Fragment page: $page")
                    // Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                    viewModelPopular.getNextPage(page)
                    // hideProgressDialog()
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

        fetchData()
    }

    private fun fetchData() {
        runBlocking {
            viewModelTR.tRMovieResponse.observe(viewLifecycleOwner) {
                adapterTR.updateList(it)
            }

            viewModelPopular.popularMovieResponse.observe(viewLifecycleOwner) {
                adapterPopular.updateList(it)
            }
        }
    }

    private fun setupViews() {
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.title = ""
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        SPAN_COUNT = if (isLandscape) 5 else 2
        setupBackButtonCallback()
        setupGridButtonClickListener()
        setupRecyclerViews()
    }

    private fun setupBackButtonCallback() {
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

    private fun setupGridButtonClickListener() {
        binding.gridBtn.setOnClickListener {
            viewType = !viewType
            switchRecyclerViewLayout()
        }
    }

    private fun setupRecyclerViews() {
        binding.rvPopularMovies.layoutManager = LinearLayoutManager(requireContext())
        adapterPopular = PopularMovieAdapter(object : PopularMovieAdapter.OnItemClickListener {
            override fun onItemClick(movie: ResultPopular) {
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                    Constants.POPULAR,
                    movie.id,
                )
                findNavController().navigate(action)
            }
        })
        binding.rvPopularMovies.adapter = adapterPopular

        binding.rvPopularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    page++
                    viewModelPopular.getNextPage(page)
                }
            }
        })

        binding.rvTopRatedMovies.layoutManager =
            GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false)
        adapterTR = TopRatedMovieAdapter(object : TopRatedMovieAdapter.OnItemClickListener {
            override fun onItemClick(movie: ResultTopRated) {
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                    Constants.TOP_RATED,
                    movie.id,
                )
                findNavController().navigate(action)
            }
        })
        binding.rvTopRatedMovies.adapter = adapterTR
    }

    override fun onResume() {
        super.onResume()
        viewModelPopular.popularMovieResponse.observe(viewLifecycleOwner) {
            adapterPopular.updateList(it)
        }
        viewModelTR.tRMovieResponse.observe(viewLifecycleOwner) {
            adapterTR.updateList(it)
        }
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

    private fun switchRecyclerViewLayout() {
        binding.rvPopularMovies.layoutManager =
            if (viewType) {
                GridLayoutManager(requireContext(), SPAN_COUNT)
            } else {
                LinearLayoutManager(requireContext())
            }
        adapterPopular.setViewType(
            if (viewType) {
                PopularMovieAdapter.ViewType.GRID
            } else {
                PopularMovieAdapter.ViewType.LIST
            },
        )
        binding.gridBtn.setImageResource(
            if (viewType) {
                R.drawable.list_view
            } else {
                R.drawable.grid_view
            },
        )
        binding.rvPopularMovies.adapter?.notifyDataSetChanged()
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

    companion object {
        private var SPAN_COUNT = 2
    }
}

package com.example.movieapp.view

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.Constants
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.model.movieSearchResponse.SearchResult
import com.example.movieapp.view.adapters.SearchListAdapter
import com.example.movieapp.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var searchListAdapter: SearchListAdapter
    private lateinit var progressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupSearchView()
        setupToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarBackButton()
    }

    private fun setupSearchView() {
        val searchView = binding.searchView
        binding.searchLayout.setOnClickListener {
            searchView.isIconified = false
        }
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            private val searchDelayHandler = Handler()

            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchDelayHandler.removeCallbacksAndMessages(null)
                searchDelayHandler.postDelayed({
                    performSearch(newText)
                }, SEARCH_DELAY_MILLIS)
                return true
            }
        })
    }

    private fun setupToolbar() {
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.setTitle(R.string.search)
    }

    private fun setupToolbarBackButton() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun performSearch(searchQuery: String?) {
        showProgressDialog()

        searchQuery?.let {
            searchViewModel.searchMovies(searchQuery)
            searchViewModel.searchList.observe(viewLifecycleOwner) { searchResults ->
                if (searchResults != null) {
                    updateSearchResults(searchResults)
                }
            }
        }

        if (searchQuery == null) {
            updateSearchResults(emptyList())
        }

        binding.searchRV.layoutManager = LinearLayoutManager(requireContext())
        setupSearchListAdapter()
        hideProgressDialog()
    }

    private fun updateSearchResults(searchResults: List<SearchResult>) {
        searchListAdapter.updateList(searchResults)
        val noResultsVisibility = if (searchResults.isEmpty()) View.VISIBLE else View.GONE
        binding.searchBg.visibility = noResultsVisibility
        binding.noResult.visibility = noResultsVisibility
    }

    private fun setupSearchListAdapter() {
        val onItemClickListener = object : SearchListAdapter.OnItemClickListener {
            override fun onItemClick(movie: SearchResult) {
                movie.id?.let { navigateToDetailFragment(it) }
            }
        }
        searchListAdapter = SearchListAdapter(onItemClickListener)
        binding.searchRV.adapter = searchListAdapter
    }

    private fun navigateToDetailFragment(movieId: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(
            Constants.TOP_RATED,
            movieId,
        )
        findNavController().navigate(action)
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

    companion object {
        private const val SEARCH_DELAY_MILLIS = 300L
    }
}

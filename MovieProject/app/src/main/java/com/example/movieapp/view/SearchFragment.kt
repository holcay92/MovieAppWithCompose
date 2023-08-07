package com.example.movieapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.Constants
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.model.movieSearchResponse.SearchResult
import com.example.movieapp.view.adapters.SearchListAdapter
import com.example.movieapp.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var fragmentSearchBinding: FragmentSearchBinding
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var searchListAdapter: SearchListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        val searchView = fragmentSearchBinding.searchView
        fragmentSearchBinding.searchLayout.setOnClickListener {
            searchView.isIconified = false
        }
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.title = "Search"
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // performSearch(query)
                // hide keyboard
                searchView.clearFocus()
                Log.d("TAG_X", "SearchFragment onViewCreated query: $query")
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                performSearch(newText)
                Log.d("TAG_X", "SearchFragment onViewCreated newText: $newText")
                return true
            }
        })

        return fragmentSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun performSearch(searchQuery: String?) {
        searchQuery?.let {
            searchViewModel.searchMovies(searchQuery)
            searchViewModel.searchList.observe(viewLifecycleOwner) { searchResults ->
                searchListAdapter.updateList(searchResults)
                if (searchResults.isNullOrEmpty()) {
                    fragmentSearchBinding.searchBg.visibility = View.VISIBLE
                } else {
                    fragmentSearchBinding.searchBg.visibility = View.GONE
                }
            }
        }
        if (searchQuery == null) {
            Toast.makeText(requireContext(), "No search query", Toast.LENGTH_SHORT).show()
            fragmentSearchBinding.searchBg.visibility = View.VISIBLE
        }
        fragmentSearchBinding.searchRV.layoutManager = LinearLayoutManager(requireContext())

        searchListAdapter = SearchListAdapter(
            object : SearchListAdapter.OnItemClickListener {
                override fun onItemClick(movie: SearchResult) {
                    val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(
                        Constants.TOP_RATED,
                        movie.id!!,
                    )

                    findNavController().navigate(action)
                }
            },
        )
        fragmentSearchBinding.searchRV.adapter = searchListAdapter

        // Observe search results from ViewModel and update the adapter
        searchViewModel.searchList.observe(viewLifecycleOwner) { searchResults ->
            searchListAdapter.updateList(searchResults)
        }
    }
}

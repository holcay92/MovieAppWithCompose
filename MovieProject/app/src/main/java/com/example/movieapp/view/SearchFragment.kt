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
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var searchListAdapter: SearchListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val searchView = binding.searchView
        binding.searchLayout.setOnClickListener {
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun performSearch(searchQuery: String?) {
        searchQuery?.let {
            viewModel.searchMovies(searchQuery)
            viewModel.searchList.observe(viewLifecycleOwner) { searchResults ->
                searchListAdapter.updateList(searchResults)
                if (searchResults.isNullOrEmpty()) {
                    binding.searchBg.visibility = View.VISIBLE
                } else {
                    binding.searchBg.visibility = View.GONE
                }
            }
        }
        if (searchQuery == null) {
            Toast.makeText(requireContext(), "No search query", Toast.LENGTH_SHORT).show()
            binding.searchBg.visibility = View.VISIBLE
        }
        binding.searchRV.layoutManager = LinearLayoutManager(requireContext())

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
        binding.searchRV.adapter = searchListAdapter

        // Observe search results from ViewModel and update the adapter
        viewModel.searchList.observe(viewLifecycleOwner) { searchResults ->
            searchListAdapter.updateList(searchResults)
        }
    }
}

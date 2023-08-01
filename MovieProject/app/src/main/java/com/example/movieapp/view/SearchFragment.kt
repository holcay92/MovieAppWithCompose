package com.example.movieapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchQuery = arguments?.getString(ARG_SEARCH_QUERY)
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
        // Fetch search results based on the search query
        searchQuery?.let {
            viewModel.searchMovies(searchQuery)
            viewModel.searchList.observe(viewLifecycleOwner) { searchResults ->
                searchListAdapter.updateList(searchResults)
                if (searchResults.isNullOrEmpty()) {
                    binding.searchBg.visibility = View.VISIBLE
                    Log.d("TAG_X", "SearchFragment onViewCreated searchResults: $searchResults")
                    Toast.makeText(requireContext(), "No search results", Toast.LENGTH_SHORT)
                        .show()
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
        binding.searchRV.adapter = searchListAdapter

        // Observe search results from ViewModel and update the adapter
        viewModel.searchList.observe(viewLifecycleOwner) { searchResults ->
            searchListAdapter.updateList(searchResults)
        }
    }

    companion object {
        var ARG_SEARCH_QUERY = "search_query"
    }
}

package com.example.movieapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.Constants
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.model.movieSearchResponse.SearchResult
import com.example.movieapp.view.adapters.SearchListAdapter
import com.example.movieapp.view.homePage.MainFragmentDirections
import com.example.movieapp.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchQuery = arguments?.getString(ARG_SEARCH_QUERY)
        Log.d("TAG_X", "SearchFragment onViewCreated: $searchQuery")

        // Fetch search results based on the search query
        if (!searchQuery.isNullOrEmpty()) {
            viewModel.searchMovies(searchQuery)
        }

        // Setup RecyclerView and adapter for displaying search results
        val searchListAdapter = SearchListAdapter(object : SearchListAdapter.OnItemClickListener {
            override fun onItemClick(movie: SearchResult) {
                Log.d("TAG_X", "SearchFragment onItemClick: $movie")
                val action :NavDirections = SearchFragmentDirections.actionSearchFragmentToDetailFragment( Constants.POPULAR,movie.id)
                Log.d("TAG_X", "SearchFragment onItemClick action: $action")

                findNavController().navigate(action)
            }
        })
        binding.searchRV.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRV.adapter = searchListAdapter

        // Observe search results from ViewModel and update the adapter
        viewModel.searchList.observe(viewLifecycleOwner) { searchResults ->
            searchListAdapter.updateList(searchResults)
        }
    }

    companion object {
        private const val ARG_SEARCH_QUERY = "search_query"

        fun newInstance(searchQuery: String): SearchFragment {
            val args = Bundle().apply {
                putString(ARG_SEARCH_QUERY, searchQuery)
            }
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

package com.example.movieapp.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
    private lateinit var fragmentSearchBinding: FragmentSearchBinding
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var searchListAdapter: SearchListAdapter
    private lateinit var progressDialog: Dialog
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
        toolbar.supportActionBar?.setTitle(R.string.search)
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // performSearch(query)
                // hide keyboard
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                showProgressDialog()
                performSearch(newText) // instant search
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
                    fragmentSearchBinding.noResult.visibility = View.VISIBLE
                } else {
                    hideProgressDialog()
                    fragmentSearchBinding.searchBg.visibility = View.GONE
                    fragmentSearchBinding.noResult.visibility = View.GONE
                }
            }
        }

        if (searchQuery == null) {
            fragmentSearchBinding.searchBg.visibility = View.VISIBLE
            fragmentSearchBinding.noResult.visibility = View.GONE
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

    private fun showProgressDialog() {
        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.setCancelable(false)
        progressDialog.show()
        Log.d("TAGX", "showProgressDialog started  ")
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }
}

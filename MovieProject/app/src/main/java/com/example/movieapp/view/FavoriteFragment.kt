package com.example.movieapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.Constants
import com.example.movieapp.databinding.FragmentFavoriteBinding
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.view.adapters.FavoriteMovieAdapter
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private val viewModel by viewModels<FavoriteMovieViewModel>()
    private lateinit var adapter: FavoriteMovieAdapter
    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set toolbars text as favorites
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.title = "Favorites"

        adapter = FavoriteMovieAdapter(
            object : FavoriteMovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: FavoriteMovie) {
                    val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                        Constants.POPULAR,
                        movie.id!!
                    )
                    findNavController().navigate(action)
                }
            }
        )
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteRecyclerView.adapter = adapter
        viewModel.favMovieList.observe(viewLifecycleOwner) {
            adapter.updateList(it)
            Log.d("TAG_X", "FavoriteFragment onViewCreated viewmodel.observe: $it")
        }


    }

}

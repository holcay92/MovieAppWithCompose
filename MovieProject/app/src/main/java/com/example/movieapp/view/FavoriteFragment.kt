package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.Constants
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentFavoriteBinding
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.view.adapters.FavoriteMovieAdapter
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(), FavoriteMovieAdapter.OnRemoveFavoriteClickListener {
    private val favoriteMovieViewModel by viewModels<FavoriteMovieViewModel>()
    private lateinit var favoriteMovieAdapter: FavoriteMovieAdapter
    private lateinit var fragmentFavoriteBinding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return fragmentFavoriteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set toolbar text as "Favorites"
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.favorites)
        // Enable the back button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteMovieViewModel.favMovieList.observe(viewLifecycleOwner) {
            favoriteMovieAdapter.updateList(it)
            val isListEmpty = it.isEmpty()
            fragmentFavoriteBinding.apply {
                emptyFavListImage.visibility = if (isListEmpty) View.VISIBLE else View.GONE
                emptyFavListText.visibility = if (isListEmpty) View.VISIBLE else View.GONE
                favoriteRecyclerView.visibility = if (isListEmpty) View.GONE else View.VISIBLE
            }
        }

        favoriteMovieAdapter = FavoriteMovieAdapter(
            object : FavoriteMovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: FavoriteMovie) {
                    val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                        Constants.POPULAR,
                        movie.id!!,
                    )
                    findNavController().navigate(action)
                }
            },
            this,
        )
        with(fragmentFavoriteBinding.favoriteRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteFragment.favoriteMovieAdapter
        }
        favoriteMovieViewModel.favMovieList.observe(viewLifecycleOwner) {
            favoriteMovieAdapter.updateList(it)
            val isListEmpty = it.isEmpty()
            fragmentFavoriteBinding.emptyFavListImage.visibility =
                if (isListEmpty) View.VISIBLE else View.GONE
            fragmentFavoriteBinding.emptyFavListText.visibility =
                if (isListEmpty) View.VISIBLE else View.GONE
            fragmentFavoriteBinding.favoriteRecyclerView.visibility =
                if (isListEmpty) View.GONE else View.VISIBLE
        }
    }

    override fun onRemoveFavoriteClick(movie: FavoriteMovie) {
        alertDialog(movie)
    }

    private fun alertDialog(movie: FavoriteMovie) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.warning)
        builder.setMessage(R.string.remove_fav_movie_message)
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            favoriteMovieViewModel.actionFavButton(movie)
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}

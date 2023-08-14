package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieapp.Constants
import com.example.movieapp.databinding.FragmentActorBinding
import com.example.movieapp.view.adapters.ActorMoviesAdapter
import com.example.movieapp.viewModel.ActorMoviesViewModel
import com.example.movieapp.viewModel.ActorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActorFragment : Fragment() {
    private lateinit var binding: FragmentActorBinding
    private val actorMoviesViewModel by viewModels<ActorMoviesViewModel>()
    private lateinit var actorMoviesAdapter: ActorMoviesAdapter

    private val actorViewModel by viewModels<ActorViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentActorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actorId = extractActorIdFromArguments()

        actorId.let {
            actorViewModel.getActorDetail(it)
        }
        actorViewModel.actorResponse.observe(viewLifecycleOwner) {
            binding.actorNameValue.text = it?.name
            binding.actorBirthdayValue.text = it?.birthday
            binding.actorCountryValue.text = it?.place_of_birth
            binding.actorBioValue.text = it?.biography
            Glide.with(binding.actorImage.context)
                .load("https://image.tmdb.org/t/p/w500${it?.profile_path}")
                .into(binding.actorImage)
        }

        actorMoviesViewModel.getActorMovies(actorId)
        actorMoviesViewModel.actorMovies.observe(viewLifecycleOwner) {
            actorMoviesAdapter =
                ActorMoviesAdapter(object : ActorMoviesAdapter.OnActorMovieClickListener {
                    override fun onActorMovieClick(actorMovie: com.example.movieapp.model.actorMovies.ActorMoviesCrew) {
                        val action = ActorFragmentDirections.actionActorFragmentToDetailFragment(
                            Constants.POPULAR,
                            actorMovie.id,
                        )
                        view.findNavController().navigate(action)
                    }
                })
            binding.actorMoviesRecyclerView?.adapter = actorMoviesAdapter
            binding.actorMoviesRecyclerView?.layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false,
                )
            actorMoviesAdapter.updateActorMovies(it ?: emptyList())
        }
    }

    private fun extractActorIdFromArguments(): Int {
        return ActorFragmentArgs.fromBundle(requireArguments()).actorId
    }
}

package com.example.movieapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.FragmentActorBinding
import com.example.movieapp.viewModel.ActorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActorFragment : Fragment() {
    private lateinit var binding: FragmentActorBinding

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

        actorId?.let {
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
    }
    private fun extractActorIdFromArguments(): Int {
        return ActorFragmentArgs.fromBundle(requireArguments()).actorId
    }
}

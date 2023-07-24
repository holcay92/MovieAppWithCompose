package com.example.fourthday.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.fourthday.databinding.FragmentDetailBinding
import com.example.fourthday.viewModel.PokemonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel by viewModels<PokemonDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = DetailFragmentArgs.fromBundle(requireArguments()).pokemonName
        binding.pokeTitle.text = name
        val url = DetailFragmentArgs.fromBundle(requireArguments()).pokemonUrl
        val id = url.substring(34, url.length - 1).toInt()
        viewModel.fetchPokemonDetail(id)
        binding.pokeDetail.text = url

        viewModel.pokemonDetailResponse.observe(viewLifecycleOwner) {
            updateUI()
        }
    }

    private fun updateUI() {
        val response = viewModel.pokemonDetailResponse.value
        binding.apply {
            response?.sprites?.versions?.generation_i?.red_blue?.front_transparent?.let {
                Glide.with(requireContext()).load(it).fitCenter().into(pokeImage)
            }
            pokeTitle.text = response?.name
            pokeDetail.text = response?.height.toString()
            pokeDetail2.text = response?.weight.toString()
            pokeDetail3.text = response?.moves?.get(0)?.move?.name.toString()
            pokeDetail4.text = response?.moves?.get(1)?.move?.name.toString()
            pokeDetail5.text = response?.moves?.get(2)?.move?.name.toString()
        }
    }
}

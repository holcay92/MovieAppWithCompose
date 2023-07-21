package com.example.fourthday.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fourthday.PokemonAdapter
import com.example.fourthday.PokemonViewModel
import com.example.fourthday.databinding.FragmentMainBinding
import com.example.fourthday.service.PokeApiService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel by viewModels<PokemonViewModel>()
    private var adapter =PokemonAdapter()

    @Inject lateinit var pokeApiService: PokeApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pokemonResponse.observe(viewLifecycleOwner) {
            adapter.updateList(it?.results)
            Log.d("TAG_X", "onViewCreated viewmodel.observe it.results : ${it?.results}")
        }
    }
}
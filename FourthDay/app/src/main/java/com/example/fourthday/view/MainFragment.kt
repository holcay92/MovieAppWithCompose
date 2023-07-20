package com.example.fourthday.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fourthday.service.PokeApiService
import com.example.fourthday.PokemonAdapter
import com.example.fourthday.PokemonViewModel
import com.example.fourthday.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: PokemonViewModel
    private lateinit var adapter: PokemonAdapter
    private lateinit var apiService : PokeApiService


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



    }

}
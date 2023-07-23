package com.example.fourthday.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fourthday.viewModel.PokemonViewModel
import com.example.fourthday.databinding.FragmentMainBinding
import com.example.fourthday.model.Pokemon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel by viewModels<PokemonViewModel>()
    private lateinit var adapter: PokemonAdapter

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
        binding.rv.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = PokemonAdapter(
            object : PokemonAdapter.OnItemClickListener {
                override fun onItemClick(pokemon: Pokemon) {
                    val navigation =
                        MainFragmentDirections.actionMainFragmentToDetailFragment(pokemon.name!!,pokemon.url!!)
                    findNavController().navigate(navigation)

                    Log.d("TAG_X", "Main Fragment onItemClick in the fragment: $pokemon")
                }
            }
        )
        binding.rv.adapter = adapter
        viewModel.pokemonResponse.observe(viewLifecycleOwner) {
            adapter.updateList(it?.results)
            Log.d("TAG_X", "Main Fragment onViewCreated viewmodel.observe it.results : ${it?.results}")


            binding.apply {
                btnNext.setOnClickListener {
                    viewModel.loadNextSet()
                }
                btnPrevious.setOnClickListener {
                    viewModel.loadPreviousSet()
                }
            }
        }
    }
}
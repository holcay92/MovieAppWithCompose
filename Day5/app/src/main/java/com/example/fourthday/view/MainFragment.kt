package com.example.fourthday.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fourthday.R
import com.example.fourthday.databinding.FragmentMainBinding
import com.example.fourthday.viewModel.PokemonViewModel

import com.example.fourthday.model.Pokemon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel by viewModels<PokemonViewModel>()
    private lateinit var adapter: PokemonAdapter
    private lateinit var progressDialog: Dialog

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
                    val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                        pokemon.name!!,
                        pokemon.url!!
                    )
                    findNavController().navigate(action)
                    Log.d("TAG_X", "Main Fragment onItemClick in the fragment: $pokemon")
                }
            }
        )
        binding.rv.adapter = adapter
        viewModel.pokemonResponse.observe(viewLifecycleOwner) {
            showProgressDialog()
            adapter.updateList(it?.results)
            Log.d(
                "TAG_X",
                "Main Fragment onViewCreated viewmodel.observe it.results : ${it?.results}"
            )
            //hideProgressDialog()
        }

        binding.apply {
            btnNext.setOnClickListener {
                showProgressDialog()
                viewModel.loadNextSet()
               // hideProgressDialog()
            }
            btnPrevious.setOnClickListener {
                viewModel.loadPreviousSet()
            }
        }
    }

    private fun showProgressDialog() {

        progressDialog = Dialog(requireContext())
        Log.d("TAG_X", "Main Fragment showProgressDialog in the fragment: $progressDialog")
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.setCancelable(false)
        progressDialog.show()
        Log.d("TAG_X", "Main Fragment showProgressDialog in the fragment1: $progressDialog")
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }


}
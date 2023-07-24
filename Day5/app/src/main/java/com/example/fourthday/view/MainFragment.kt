package com.example.fourthday.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import kotlinx.coroutines.runBlocking

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
        showProgressDialog()
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
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                alertDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        binding.rv.adapter = adapter
        viewModel.pokemonResponse.observe(viewLifecycleOwner) {

            adapter.updateList(it?.results)
            hideProgressDialog()
        }

        binding.apply {
            btnNext.setOnClickListener {
                showProgressDialog()
                viewModel.loadNextSet()
                hideProgressDialog()
            }
            btnPrevious.setOnClickListener {
                showProgressDialog()
                viewModel.loadPreviousSet()
                hideProgressDialog()
            }
        }
    }

    private fun showProgressDialog() {
        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        val handler = Handler()
        val progressBarDelay = 500L
        handler.postDelayed({
            progressDialog.dismiss()
        }, progressBarDelay)
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alert")
        builder.setMessage("Do you want to exit?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { _, _ ->
            requireActivity().finish() // Exit the app
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
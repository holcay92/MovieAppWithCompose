package com.example.fourthday

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fourthday.databinding.ItemPokeBinding
import com.example.fourthday.model.Pokemon
import com.example.fourthday.model.Result



class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

   private val pokemonList: MutableList<Pokemon> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemPokeBinding.inflate(inflater, parent, false)
        return PokemonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.bind(pokemon)
    }

    override fun getItemCount() = pokemonList.size


    fun updateData(newPokemonList: List<Pokemon>) {
        pokemonList.clear()
        pokemonList.addAll(newPokemonList)
        notifyDataSetChanged()
    }

    inner class PokemonViewHolder(private val binding: ItemPokeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: Pokemon) {
            binding.pokeTitle.text = pokemon.name
        }
    }
}


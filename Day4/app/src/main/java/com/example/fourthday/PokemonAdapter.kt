package com.example.fourthday

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fourthday.databinding.ItemPokeBinding
import com.example.fourthday.model.Pokemon


class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    private var pokemonList = arrayListOf<Pokemon>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val itemView =
           ItemPokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(itemView)
    }
    override fun getItemCount() = pokemonList.size

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        return holder.bind(pokemonList[position])
    }

    class PokemonViewHolder(bindingItem: ItemPokeBinding) : RecyclerView.ViewHolder(bindingItem.root) {
        fun bind(pokemon: Pokemon) {
            val bindingItem = ItemPokeBinding.bind(itemView)
            bindingItem.apply {
          pokeTitle.text = pokemon.name
                Log.d("TAG_X", "bind in the adapter pokemon.name : ${pokemon.name}")
            }
        }
    }

    fun updateList(list: List<Pokemon>?) {
        pokemonList.clear()
        Log.d("TAG_X", "updateList in the adapter: $list")
        pokemonList.addAll(list ?: emptyList())
        Log.d("TAG_X", "updateList in the adapter pokemonlist: $pokemonList")
        notifyDataSetChanged()
    }


}

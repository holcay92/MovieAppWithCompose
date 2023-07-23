package com.example.fourthday

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fourthday.databinding.ItemPokeBinding
import com.example.fourthday.model.Pokemon
import com.example.fourthday.model.PokemonResponse


class PokemonAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    private var pokemonList: ArrayList<Pokemon> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val itemView =
            ItemPokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("TAG_X", "onCreateViewHolder in the adapter: $pokemonList")
        Log.d("TAG_X", "onCreateViewHolder in the adapter itemView: $itemView")
        return PokemonViewHolder(itemView)
    }

    override fun getItemCount() = pokemonList.size

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        Log.d("TAG_X", "onBindViewHolder in the adapter: $pokemonList")
        holder.bind(pokemonList[position])
        holder.itemView.setOnClickListener {
            listener?.onItemClick(pokemonList[position])
        }
    }

    class PokemonViewHolder(bindingItem: ItemPokeBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
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

    interface OnItemClickListener {
        fun onItemClick(pokemon: Pokemon)
    }


}

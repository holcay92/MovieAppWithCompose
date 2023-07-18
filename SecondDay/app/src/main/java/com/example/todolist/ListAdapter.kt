package com.example.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ListItemBinding
import com.example.todolist.db.Item


class ListAdapter(private val clickListener: (Item) -> Unit) : RecyclerView.Adapter<ItemViewHolder>(){

    private val itemList= arrayListOf<Item>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //layout object is created
        val bindingItem = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(bindingItem)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        return holder.bind(itemList[position],clickListener)
    }

    override fun getItemCount(): Int {
        //return the size of the list
        return itemList.size
    }

    fun setList(list:List<Item>){
        //thats because we want to update the list
        itemList.clear()
        itemList.addAll(list)
    }
}
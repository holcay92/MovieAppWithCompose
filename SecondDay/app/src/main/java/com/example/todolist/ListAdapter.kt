package com.example.todolist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ListItemBinding
import com.example.todolist.db.Item


class ListAdapter(private val clickListener: (Item) -> Unit) :
    RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {
    private val itemList = arrayListOf<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //layout object is created
        val bindingItem =
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(bindingItem)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        return holder.bind(itemList[position], clickListener)
    }

    override fun getItemCount() = itemList.size


    class ItemViewHolder(private val bindingItem: ListItemBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        fun bind(item: Item, clickListener: (Item) -> Unit) {

            val bindingItem = ListItemBinding.bind(itemView)
            bindingItem.apply {
                itemTitle.text = item.title
                itemDetail.text = item.detail
                Log.d("ItemViewHolder", "item: $item title ${item.title} detail ${item.detail}")
                root.setOnClickListener {
                    clickListener(item)
                }
                Log.d("ItemViewHolder1", "item: $item title ${item.title} detail ${item.detail}")

            }
        }
    }

    fun setList(list: List<Item>) {
        //that's because we want to update the list
        Log.d("MainActivityTest setList itemlist in ListAdapter", "itemlist: $itemList")
        itemList.clear()
        Log.d("MainActivityTest setList in ListAdapter", "list: $list")
        itemList.addAll(list)
        notifyDataSetChanged()
    }

}
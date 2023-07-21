package com.example.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ListItemBinding
import com.example.todolist.db.Item


class ListAdapter(private val clickListener: (Item) -> Unit) : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {
    private val itemList = arrayListOf<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
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
                root.setOnClickListener {
                    clickListener(item)
                }
            }
        }
    }

    fun setList(list: List<Item>) {
        //that's because we want to update the list
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

}
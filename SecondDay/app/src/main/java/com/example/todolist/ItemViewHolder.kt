package com.example.todolist

import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ListItemBinding
import com.example.todolist.db.Item

class ItemViewHolder(private val bindingItem: ListItemBinding) :
    RecyclerView.ViewHolder(bindingItem.root) {
    fun bind(item: Item, clickListener: (Item) -> Unit) {

        bindingItem.apply {
            itemTitle.setText(item.title)
            itemDetail.setText(item.detail)
            root.setOnClickListener {
                clickListener(item)
            }
        }
    }
}
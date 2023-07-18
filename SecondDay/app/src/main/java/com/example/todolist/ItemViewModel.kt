package com.example.todolist

import androidx.lifecycle.ViewModel
import com.example.todolist.db.Item
import com.example.todolist.db.ItemDao

class ItemViewModel(private val dao: ItemDao): ViewModel() {

    val items = dao.getAllItems()

    suspend fun insert(item: Item) {
        dao.insertItem(item)
    }

    suspend fun update(item: Item) {
        dao.updateItem(item)
    }

    suspend fun delete(item: Item) {
        dao.deleteItem(item)
    }
}
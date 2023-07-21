package com.example.todolist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.db.Item
import com.example.todolist.db.ItemDao
import kotlinx.coroutines.launch

class ItemViewModel(private val dao: ItemDao) : ViewModel() {

    val items: LiveData<List<Item>> = dao.getAllItems()

    fun insert(item: Item) = viewModelScope.launch {
        dao.insertItem(item)
    }

    fun update(item: Item) = viewModelScope.launch {
        dao.updateItem(item)
    }

    fun delete(item: Item) = viewModelScope.launch {
        dao.deleteItem(item)
    }
}
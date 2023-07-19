package com.example.todolist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.db.ItemDao

class ItemViewModelFactory (private val dao: ItemDao): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)){
            Log.d("ItemViewModelFactory", "create: ")
            return ItemViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
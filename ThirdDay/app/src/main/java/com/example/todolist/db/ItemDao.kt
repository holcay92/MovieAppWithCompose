package com.example.todolist.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao

interface ItemDao {

    @Insert
    suspend fun insertItem(item: Item)

    @Update
    suspend fun updateItem(item: Item): Int

    @Delete
    suspend fun deleteItem(item: Item) : Int

    @Query("SELECT * FROM item_data_table ORDER BY item_id ASC")
    fun getAllItems(): LiveData<List<Item>>

}
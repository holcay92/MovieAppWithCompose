package com.example.todolist.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_data_table")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "item_title")
    val title: String,
    @ColumnInfo(name = "item_detail")
    val detail: String,
)

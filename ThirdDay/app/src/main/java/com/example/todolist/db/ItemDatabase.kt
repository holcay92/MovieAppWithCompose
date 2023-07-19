package com.example.todolist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemDatabase:RoomDatabase() {
    abstract fun itemDao():ItemDao
    // singleton pattern
    companion object {
        // @Volatile means that
        // this variable will be visible to all threads immediately
        @Volatile
        private var INSTANCE: ItemDatabase? = null
        fun getInstance(context: Context): ItemDatabase {
            synchronized(this) {
                var instance = INSTANCE
                // if the instance is null then create a new database
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ItemDatabase::class.java,
                        "item_database"
                    ).build()
                }
                return instance
            }
        }
    }
}
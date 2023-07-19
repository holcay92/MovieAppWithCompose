package com.example.todolist

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.IntentCompat.getParcelableExtra
import com.example.todolist.databinding.ActivityDetailBinding
import com.example.todolist.db.Item
import com.example.todolist.db.ItemDatabase

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: ItemViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


            val item = intent.getParcelableExtra("item", Item::class.java)
        Log.d("DetailActivityTest", "item: $item")
            binding.itemTitle.setText(item?.title)
            binding.itemDetail.setText(item?.detail)




        val dao = ItemDatabase.getInstance(application).itemDao()
        val factory = ItemViewModelFactory(dao)
        viewModel = factory.create(ItemViewModel::class.java)

        binding.btnSave.setOnClickListener {
            saveItem()
            clearInput()
            finish()
        }

        binding.btnEdit.setOnClickListener {
            updateItem()
            clearInput()
            finish()
        }

        binding.btnDelete.setOnClickListener {
            //  viewModel.delete(selectedItem)
            clearInput()
            finish()
        }
    }


    private fun saveItem() {
        val title = binding.itemTitle.text.toString()
        val detail = binding.itemDetail.text.toString()
        val item = Item(0, title, detail)
        viewModel.insert(item)

    }

    private fun updateItem() {
        val title = binding.itemTitle.text.toString()
        val detail = binding.itemDetail.text.toString()
        // val item = Item(selectedItem.id, title, detail)
        //viewModel.update(item)
    }

    private fun clearInput() {
        binding.apply {
            itemTitle.setText("")
            itemDetail.setText("")
        }
    }


}
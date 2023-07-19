package com.example.todolist

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
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
        val dao = ItemDatabase.getInstance(application).itemDao()
        val factory = ItemViewModelFactory(dao)
        viewModel = factory.create(ItemViewModel::class.java)

        val selectedItem = intent.getParcelableExtra("item", Item::class.java)
        val isItemSelected = intent.getBooleanExtra("itemUpdate", false)

        binding.itemTitle.setText(selectedItem?.title)
        binding.itemDetail.setText(selectedItem?.detail)
        if (isItemSelected) {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnSave.text = "Update"
        }

        binding.btnSave.setOnClickListener {
            if (isItemSelected) {
                updateItem()
            } else {
                saveItem()
            }
            clearInput()
            finish()
        }
        binding.btnDelete.setOnClickListener {
            Log.d("DetailActivityTest", selectedItem.toString())
            deleteItem(selectedItem!!)
            clearInput()
            Log.d("DetailActivityTest", "delete")
            finish()


        }
    }

    override fun onStop() {
        super.onStop()
        binding.btnSave.text = "Save"
        binding.btnDelete.visibility = View.GONE

    }


    private fun saveItem() {
        val title = binding.itemTitle.text.toString()
        val detail = binding.itemDetail.text.toString()
        val item = Item(0, title, detail)
        viewModel.insert(item)

    }
    private fun deleteItem(selectedItem: Item) {
        Log.d("DetailActivityTest", "selectedItem $selectedItem")
        viewModel.delete(Item(selectedItem.id, selectedItem.title, selectedItem.detail))
        Log.d("DetailActivityTest", "delete")
    }

    private fun updateItem() {
        val title = binding.itemTitle.text.toString()
        val detail = binding.itemDetail.text.toString()
        val item = Item(0, title, detail)
        viewModel.update(item)
    }

    private fun clearInput() {
        binding.apply {
            itemTitle.setText("")
            itemDetail.setText("")
        }
    }




}
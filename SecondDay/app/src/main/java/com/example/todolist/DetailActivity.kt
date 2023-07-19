package com.example.todolist

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityDetailBinding
import com.example.todolist.db.Item
import com.example.todolist.db.ItemDatabase
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: ItemViewModel
    private lateinit var selectedItem: Item
    private var choosenItem :Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get the selected item from the intent
        choosenItem = intent.getIntExtra("selected_item", 0)
        Log.d("DetailActivity", "onCreate: $choosenItem")
        // set the selected item to the input fields
        binding.itemTitle.setText(selectedItem.title)
        binding.itemDetail.setText(selectedItem.detail)


        val dao = ItemDatabase.getInstance(application).itemDao()
        val factory = ItemViewModelFactory(dao)
        viewModel = factory.create(ItemViewModel::class.java)

        binding.btnSave.setOnClickListener {
            saveItem()
            clearInput()
            // go back to main activity
            finish()
        }

        binding.btnEdit.setOnClickListener {
            updateItem()
            clearInput()
            finish()
        }

        binding.btnDelete.setOnClickListener {
            viewModel.delete(selectedItem)
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
        val item = Item(selectedItem.id, title, detail)
        viewModel.update(item)
    }
    private fun clearInput() {
        binding.apply {
            itemTitle.setText("")
            itemDetail.setText("")
        }
    }
    @SuppressLint("NotifyDataSetChanged")
     fun displayStudentList(adapter: ListAdapter) {
        viewModel.items.observe(this) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }


}
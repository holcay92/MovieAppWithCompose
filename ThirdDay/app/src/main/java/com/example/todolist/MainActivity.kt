package com.example.todolist


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.db.Item
import com.example.todolist.db.ItemDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ItemViewModel
    private lateinit var adapter: ListAdapter
    private var isListItemSelected = false
    private lateinit var selectedItem: Item
    companion object {
       var itemUpdate :Boolean = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.setHasFixedSize(true)
        val dao = ItemDatabase.getInstance(application).itemDao()
        val factory = ItemViewModelFactory(dao)
        viewModel = factory.create(ItemViewModel::class.java)

        adapter = ListAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("item", it)
            startActivity(intent)
        }
            firstInitRecyclerView()
            initRecyclerView()

        binding.btnCreate.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
        firstInitRecyclerView()

    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
        firstInitRecyclerView()
    }

    private fun initRecyclerView() {
        viewModel.items.observe(this) { items ->
                adapter.setList(items)
            if (items.isEmpty()) {
                binding.rv.visibility = View.GONE
                binding.emptyMessage.visibility = View.VISIBLE
            } else {
                displayItemList()
                binding.rv.visibility = View.VISIBLE
                binding.emptyMessage.visibility = View.GONE
            }
        }
    }
    private fun firstInitRecyclerView() {
        binding.apply {
            rv.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ListAdapter { selectedItem: Item ->
                listItemClicked(selectedItem)
            }

            rv.adapter = adapter

            displayItemList()
        }


    }


    private fun displayItemList() {
        Log.d("MainActivityTest", "displayItemList")
        viewModel.items.observe(this) {
            Log.d("MainActivityTest", "setListbefore")
            adapter.setList(it)

            adapter.notifyDataSetChanged()
            Log.d("MainActivityTest", "setListafter, adapter= $adapter")

        }
    }

    private fun listItemClicked(item: Item) {
        itemUpdate = true
        binding.apply{
            selectedItem = item
            isListItemSelected = true

            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("item", item)
            intent.putExtra("itemUpdate", itemUpdate)
            startActivity(intent)

        }
    }
}



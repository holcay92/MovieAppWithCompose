package com.example.todolist


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.db.Item
import com.example.todolist.db.ItemDatabase

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListAdapter

    private var isListItemSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeDataFromDatabase()
        initRecyclerView()
        binding.btnCreate.setOnClickListener {
            // go to detail activity
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
            initRecyclerView()
        }

    }
    private fun observeDataFromDatabase() {
        val dataObserver = Observer<List<Item>> { items ->
            if (items.isNullOrEmpty()) {
                // No records in the database
                binding.rv.visibility = View.GONE
                binding.emptyMessage.visibility = View.VISIBLE
            } else {
                // Records exist in the database
                binding.rv.visibility = View.VISIBLE
                binding.emptyMessage.visibility = View.GONE
                adapter.setList(items)
            }
        }

        val data = getDataFromDatabase()
        data.observe(this, dataObserver)
    }
    private fun getDataFromDatabase(): LiveData<List<Item>> {
        val database = ItemDatabase.getInstance(this)
        val dao = database.itemDao()

        return dao.getAllItems()
    }

    private fun initRecyclerView() {
        binding.apply {
            rv.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ListAdapter { selectedItem: Item ->
                //listItemClicked(selectedItem)
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("selected_item", selectedItem.id)
                startActivity(intent)

            }
            rv.adapter = adapter
            //displayItemList()
        }


    }









}
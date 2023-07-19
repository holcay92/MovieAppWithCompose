package com.example.todolist


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.db.ItemDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ItemViewModel
    private lateinit var adapter: ListAdapter


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
            // todo item click parcelable
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("item", it)
            startActivity(intent)

        }


        //TODO list manipulation
        initRecyclerView(adapter, binding)

        binding.btnCreate.setOnClickListener {
            // go to detail activity
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)

        }

    }

    override fun onResume() {
        super.onResume()
        initRecyclerView(adapter, binding)
    }

    private fun initRecyclerView(adapter: ListAdapter, bindingMain: ActivityMainBinding) {
        bindingMain.rv.layoutManager = LinearLayoutManager(this)
        bindingMain.rv.adapter = adapter

        //get all items from db
        displayStudentList()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayStudentList() {
        viewModel.items.observe(this) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }

    }


}



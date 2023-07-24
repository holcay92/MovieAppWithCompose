package com.example.todolist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.ListAdapter
import com.example.todolist.R
import com.example.todolist.databinding.FragmentRecyclerBinding
import com.example.todolist.db.Item
import com.example.todolist.db.ItemDatabase
import com.example.todolist.viewModel.ItemViewModel
import com.example.todolist.viewModel.ItemViewModelFactory

class RecyclerFragment : Fragment() {

    private lateinit var binding: FragmentRecyclerBinding
    private lateinit var viewModel: ItemViewModel
    private lateinit var adapter: ListAdapter
    private var isListItemSelected = false
    private lateinit var selectedItem: Item

    var itemUpdate: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.setHasFixedSize(true)

        val dao = ItemDatabase.getInstance(requireContext()).itemDao()
        val factory = ItemViewModelFactory(dao)
        viewModel = factory.create(ItemViewModel::class.java)

        adapter = ListAdapter {
            parentFragmentManager.commit {
                replace<DetailFragment>(R.id.fragment_container_view)
                addToBackStack(null)
            }

            firstInitRecyclerView()
            initRecyclerView()
        }
        binding.btnCreate.setOnClickListener {
            parentFragmentManager.commit {
                selectedItem = Item(0, "", "")
                val bundle = bundleOf(ITEM to selectedItem, ITEM_UPDATE to itemUpdate)
                replace<DetailFragment>(R.id.fragment_container_view, args = bundle)
                addToBackStack(null)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
        firstInitRecyclerView()
    }

    private fun initRecyclerView() {
        viewModel.items.observe(requireActivity()) { items ->
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
            rv.layoutManager = LinearLayoutManager(requireContext())
            adapter = ListAdapter { selectedItem: Item ->
                listItemClicked(selectedItem)
            }
            rv.adapter = adapter
            displayItemList()
        }
    }

    private fun displayItemList() {
        viewModel.items.observe(requireActivity()) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun listItemClicked(item: Item) {
        itemUpdate = true
        binding.apply {
            selectedItem = item
            isListItemSelected = true

            parentFragmentManager.commit {
                val bundle = bundleOf(ITEM to item, ITEM_UPDATE to itemUpdate)
                replace<DetailFragment>(R.id.fragment_container_view, args = bundle)
                addToBackStack(null)
            }
        }
    }

    companion object {
        const val ITEM = "item"
        const val ITEM_UPDATE = "itemUpdate"
    }
}

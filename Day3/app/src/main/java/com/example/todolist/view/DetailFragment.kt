package com.example.todolist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todolist.viewModel.ItemViewModel
import com.example.todolist.viewModel.ItemViewModelFactory
import com.example.todolist.databinding.FragmentDetailBinding
import com.example.todolist.db.Item
import com.example.todolist.db.ItemDatabase
import com.example.todolist.view.RecyclerFragment.Companion.ITEM
import com.example.todolist.view.RecyclerFragment.Companion.ITEM_UPDATE
import kotlinx.coroutines.runBlocking

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: ItemViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val dao = ItemDatabase.getInstance(requireContext()).itemDao()
        val factory = ItemViewModelFactory(dao)
        viewModel = factory.create(ItemViewModel::class.java)

        val selectedItem = requireArguments().getParcelable<Item>(ITEM)
        val isItemSelected = requireArguments().getBoolean(ITEM_UPDATE, false)
        prepareUI(selectedItem, isItemSelected)

        binding.btnSave.setOnClickListener {
            if (isItemSelected) {
                updateItem(selectedItem!!)
            } else {
                saveItem()
            }
            clearInput()
            parentFragmentManager.popBackStack()
        }
        binding.btnDelete.setOnClickListener {
            deleteItem(selectedItem!!)
            clearInput()
            parentFragmentManager.popBackStack()
        }
    }

    private fun saveItem() {
        val title = binding.itemTitle.text.toString()
        val detail = binding.itemDetail.text.toString()
        val item = Item(0, title, detail)
        runBlocking {
            viewModel.insert(item)
        }
    }
    private fun deleteItem(selectedItem: Item) {
        runBlocking {
            viewModel.delete(selectedItem)
        }
    }
    private fun updateItem(selectedItem: Item) {
        val title = binding.itemTitle.text.toString()
        val detail = binding.itemDetail.text.toString()
        val item = Item(selectedItem.id, title, detail)
        runBlocking {
            viewModel.update(item)
        }
    }
    private fun clearInput() {
        binding.apply {
            itemTitle.setText("")
            itemDetail.setText("")
        }
    }
    private fun prepareUI(selectedItem: Item?, isItemSelected: Boolean) {
        binding.itemTitle.setText(selectedItem?.title)
        binding.itemDetail.setText(selectedItem?.detail)
        if (isItemSelected) {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnSave.text = "Update"
        }
    }
}

package com.example.todolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.todolist.databinding.FragmentRecyclerBinding
import androidx.fragment.app.setFragmentResultListener

class RecyclerFragment : Fragment() {

    private lateinit var binding: FragmentRecyclerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  setFragmentResultListener(REQUEST_KEY) { requestKey, bundle ->
        //    Log.i("RESULT HERE", bundle.getString("key") ?: "")
        //  }
        binding.btnCreate.setOnClickListener {
            parentFragmentManager.commit {
                val bundle = bundleOf("text" to "merhaba 2")
                replace<DetailFragment>(R.id.fragment_container_view, args = bundle)
                addToBackStack(null)
            }
        }
    }

    companion object {
        const val REQUEST_KEY = "result"
    }

}



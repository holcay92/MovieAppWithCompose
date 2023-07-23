package com.example.fourthday.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fourthday.R
import com.example.fourthday.databinding.ActivityFragmentContainerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentContainerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFragmentContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
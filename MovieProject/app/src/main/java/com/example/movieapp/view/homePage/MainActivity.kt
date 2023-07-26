package com.example.movieapp.view.homePage

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.visibility = View.GONE

        if (savedInstanceState == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                val action = SplashFragmentDirections.actionSplashFragmentToMainFragment()
                findNavController(R.id.fragmentContainerView).navigate(action)
                binding.bottomNavigation.visibility = View.VISIBLE
            }, SPLASH_DELAY.toLong())
        }

        // home button ob the bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.mainFragment)
                    true
                }

                R.id.favorites -> {
                    // findNavController(R.id.fragmentContainerView).navigate(R.id.favoriteFragment)
                    true
                }

                else -> false
            }
        }
    }

    companion object {
        private const val SPLASH_DELAY = 1000
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.navigationBars())
            }
        }
    }
}

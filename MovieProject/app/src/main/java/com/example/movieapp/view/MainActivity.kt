package com.example.movieapp.view

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.movieapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility = View.GONE

        if (savedInstanceState == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                val action = SplashFragmentDirections.actionSplashFragmentToMainFragment()
                findNavController(R.id.fragmentContainerView).navigate(action)
                findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility = View.VISIBLE
            }, SPLASH_DELAY.toLong())
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

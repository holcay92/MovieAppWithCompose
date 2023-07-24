package com.example.movieapp.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.movieapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                val action = SplashFragmentDirections.actionSplashFragmentToMainFragment()
                findNavController(R.id.fragmentContainerView).navigate(action)
            }, SPLASH_DELAY.toLong())
        }
    }

    companion object {
        private const val SPLASH_DELAY = 2000
    }
}

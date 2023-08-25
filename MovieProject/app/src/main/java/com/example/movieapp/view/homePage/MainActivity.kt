package com.example.movieapp.view.homePage

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Set the activity to full-screen mode
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        setupBottomNavigation()
        hideSystemUI()

        if (savedInstanceState == null) {
            hideNavigationBarAndToolbar()
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToMainFragment()
                showNavigationBarAndToolbar()
            }, SPLASH_DELAY.toLong())
        }
    }

    private fun setupBottomNavigation() {
        activityMainBinding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.mainFragment)
                    true
                }

                R.id.favorites -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.favoriteFragment)
                    true
                }

                R.id.action_search_nav_bar -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.searchFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun navigateToMainFragment() {
        val action = SplashFragmentDirections.actionSplashFragmentToMainFragment()
        findNavController(R.id.fragmentContainerView).navigate(action)
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.navigationBars())
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun hideNavigationBarAndToolbar() {
        activityMainBinding.bottomNavigation.visibility = View.GONE
    }

    private fun showNavigationBarAndToolbar() {
        activityMainBinding.bottomNavigation.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object {
        private const val SPLASH_DELAY = 2100
    }
}

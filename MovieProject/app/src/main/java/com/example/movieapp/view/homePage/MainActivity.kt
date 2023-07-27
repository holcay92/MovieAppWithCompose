package com.example.movieapp.view.homePage

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.service.MovieApiService
import com.example.movieapp.view.SearchFragment
import com.example.movieapp.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var movieApiService: MovieApiService

    companion object {
        private const val SPLASH_DELAY = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        binding.bottomNavigation.visibility = View.GONE
        binding.toolbar.visibility = View.GONE

        if (savedInstanceState == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                val action = SplashFragmentDirections.actionSplashFragmentToMainFragment()
                findNavController(R.id.fragmentContainerView).navigate(action)
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.toolbar.visibility = View.VISIBLE
            }, SPLASH_DELAY.toLong())
        }

        // home button of the bottom navigation
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.navigationBars())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("TAG_X MainActivity query", query)
                searchView.clearFocus()
                val searchFragment = SearchFragment.newInstance(query)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, searchFragment)
                    .addToBackStack(null)
                    .commit()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, SearchFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
        })

        return true
    }
}

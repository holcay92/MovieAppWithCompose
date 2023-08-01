package com.example.movieapp.view.homePage

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.service.MovieApiService
import com.example.movieapp.view.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var movieApiService: MovieApiService
    private var searchJob: Job? = null
    companion object {
        private const val SPLASH_DELAY = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupBottomNavigation()
        hideSystemUI()

        if (savedInstanceState == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToMainFragment()
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.toolbar.visibility = View.VISIBLE
            }, SPLASH_DELAY.toLong())
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.mainFragment)
                    true
                }
                R.id.favorites -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.favoriteFragment)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        val searchEditText = searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        searchEditText.setTextColor(resources.getColor(R.color.light_theme))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("TAG_X ", "onQueryTextSubmit $query")
                searchView.clearFocus()
                if (query.isNotBlank()) {
                    // Handle query submission
                    navigateToSearchFragment(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                Log.d("TAG_X ", "onQueryTextChange $query")
                // Cancel the previous search job if it's still running to avoid unnecessary requests
                searchJob?.cancel()

                // Delay the search request by a small amount of time to avoid making
                // requests for every single character typed by the user.
                searchJob = lifecycleScope.launch {
                    delay(300)
                   // performSearch(newText)
                }
                return true
            }
        })

        return true
    }
    private fun navigateToSearchFragment(query: String) {
        val args = Bundle()
        args.putString(SearchFragment.ARG_SEARCH_QUERY, query)

        // Use the NavController to navigate to the SearchFragment
        findNavController(R.id.fragmentContainerView).navigate(R.id.searchFragment, args)
    }
}

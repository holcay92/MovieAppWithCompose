package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.* // ktlint-disable no-wildcard-imports
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieapp.Constants
import com.example.movieapp.R
import com.example.movieapp.model.movieSearchResponse.SearchResult
import com.example.movieapp.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setupToolbar()

        return ComposeView(requireContext()).apply {
            setContent {
                SearchScreen(findNavController())
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.setTitle(R.string.search)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

@Composable
fun SearchScreen(navController: NavController) {
    val searchViewModel: SearchViewModel = viewModel()
    val searchResponse by searchViewModel.searchList.observeAsState(emptyList())
    CustomTopAppBar("SEARCH", onBackClick = { navController.popBackStack() })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchView(
            searchViewModel,
        )
        SearchResults(searchResponse!!)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    searchViewModel: SearchViewModel,

) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val searchHistory = remember { mutableStateListOf<String>() }
    val searchScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }

    Card(modifier = Modifier.padding(top = 16.dp)) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth().background(colorResource(id = R.color.main_theme_bg)),
            query = text,
            onQueryChange = {
                text = it
                searchScope.launch {
                    searchJob?.cancel() // Cancel the previous search job
                    searchJob = launch {
                        delay(900)
                        if (text.isNotEmpty()) {
                            active = false
                        }
                        searchViewModel.searchMovies(text)
                    }
                }
            },
            onSearch = {
                active = false
                searchHistory.add(text)
                searchViewModel.searchMovies(text)
                text = ""
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search),
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = Color.Black,
                )
            },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                text = ""
                            } else {
                                active = false
                                searchViewModel.searchList.value = emptyList()
                            }
                        },
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = Color.Black,
                    )
                }
            },
        ) {
            searchHistory.forEach { historyItem ->
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                    )
                    Text(
                        text = historyItem ?: "",
                        modifier = Modifier.weight(1f),
                        color = Color.Black,
                        fontSize = 16.sp,
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable {
                                searchHistory.remove(historyItem)
                            },
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResults(movies: List<SearchResult>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        items(movies) { movie ->
            MovieItem(movie = movie)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieItem(movie: SearchResult) {
    Card(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(5.dp).clickable {
                val action = movie.id?.let {
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(
                        Constants.POPULAR,
                        it,
                    )
                }
            },
        elevation = CardDefaults.cardElevation(10.dp),

    ) {
        Row(
            modifier = Modifier
                .background(colorResource(id = R.color.main_theme_bg))
                .fillMaxWidth(),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = if (movie.isFavorite) R.drawable.add_fav_filled_icon else R.drawable.add_fav_empty_icon),
                contentDescription = null, // Provide proper description
                tint = Color.Yellow,
                modifier = Modifier
                    .size(30.dp),
            )

            Spacer(modifier = Modifier.width(10.dp))

            movie.title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f),
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            GlideImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = null,
                modifier = Modifier
                    .width(40.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent),
            )
        }
    }
}

package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.* // ktlint-disable no-wildcard-imports
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieapp.Constants
import com.example.movieapp.R
import com.example.movieapp.model.movieSearchResponse.SearchResult
import com.example.movieapp.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                SearchScreen()
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.setTitle(R.string.search)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun navigateToDetailFragment(movieId: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(
            Constants.TOP_RATED,
            movieId,
        )
        findNavController().navigate(action)
    }
}

private const val SEARCH_DELAY_MILLIS = 300L

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchView(
    query: String,
    onQueryChange: (String) -> Unit,
    onQuerySubmit: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = colorResource(id = R.color.light_theme))
            .border(
                1.dp,
                color = colorResource(id = R.color.light_bold_theme),
                RoundedCornerShape(4.dp),
            )
            .clickable {
                // Set focus on text field when clicked
                keyboardController?.show()
            },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search), // Your search icon resource
            contentDescription = null, // Provide proper description
            tint = Color.Gray,
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp),
        )
        if (query.isEmpty()) {
            Text(
                text = "Search movies...",
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp),
            )
        } else {
            BasicTextField(
                value = query,
                onValueChange = { newQuery ->
                    onQueryChange(newQuery)
                    keyboardController?.apply {
                        view.removeCallbacks(null)
                        view.postDelayed({
                            onQuerySubmit(newQuery)
                        }, SEARCH_DELAY_MILLIS)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onQuerySubmit(query)
                        keyboardController?.hide()
                    },
                ),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp).clickable {
                        // Set focus on text field when clicked
                        keyboardController?.show()
                    },

                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Composable
fun SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val searchViewModel: SearchViewModel = viewModel()
    val searchResponse by searchViewModel.searchList.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
            .padding(top = 45.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchView(
            query = searchQuery,
            onQueryChange = { newQuery ->
                searchQuery = newQuery
            },
            onQuerySubmit = { submittedQuery ->
                searchViewModel.searchMovies(submittedQuery)
            },
        )
        Spacer(modifier = Modifier.height(40.dp))
        searchResponse?.let { SearchResults(it) }

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.bino),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .alpha(0.5f),
        )
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(5.dp),
    ) {
        Icon(
            painter = painterResource(id = if (movie.isFavorite) R.drawable.add_fav_filled_icon else R.drawable.add_fav_empty_icon),
            contentDescription = null, // Provide proper description
            tint = Color.White,
            modifier = Modifier
                .size(35.dp),
        )

        Spacer(modifier = Modifier.width(10.dp))

        movie.title?.let {
            Text(
                text = it,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        GlideImage(
            model = movie.posterPath,
            contentDescription = null,
            modifier = Modifier
                .width(200.dp)
                .height(300.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Transparent),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    SearchScreen()
}

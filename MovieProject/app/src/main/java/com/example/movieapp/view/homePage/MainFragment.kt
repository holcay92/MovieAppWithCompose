package com.example.movieapp.view.homePage

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieapp.Constants
import com.example.movieapp.R
import com.example.movieapp.model.movie.MovieResult
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.view.MainTopAppBar
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import com.example.movieapp.viewModel.NowPlayingMovieViewModel
import com.example.movieapp.viewModel.PopularMovieViewModel
import com.example.movieapp.viewModel.TopRatedMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment :
    Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MainScreen(
                    navController = findNavController(),
                )
            }
        }
    }
}

const val SPAN_COUNT_6 = 6
const val SPAN_COUNT_4 = 4

@Composable
fun MainScreen(navController: NavController) {
    val popularMovieViewModel: PopularMovieViewModel = viewModel()
    val popularMovies = popularMovieViewModel.popularMovieResponse.observeAsState(emptyList())

    val topRatedMovieViewModel: TopRatedMovieViewModel = viewModel()
    val topRatedMovies = topRatedMovieViewModel.tRMovieResponse.observeAsState(emptyList())

    val nowPlayingMovieViewModel: NowPlayingMovieViewModel = viewModel()
    val nowPlayingMovies = nowPlayingMovieViewModel.nowPlayingMovies.observeAsState(emptyList())

    popularMovieViewModel.updateFavoriteResult()
    topRatedMovieViewModel.updateFavoriteResult()
    nowPlayingMovieViewModel.updateFavoriteResult()

    MainTopAppBar()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            TopRatedMoviesList(navController, topRatedMovies.value ?: emptyList())
        }
        item {
            NowPlayingMoviesList(navController, nowPlayingMovies.value ?: emptyList())
        }
        item {
            PopularMoviesList(
                navController,
                popularMovies.value ?: emptyList(),
            )
        }
    }
}

@Composable
fun NowPlayingMoviesList(navController: NavController, movies: List<MovieResult>) {
    Text(
        modifier = Modifier.padding(start = 10.dp, top = 10.dp),
        text = stringResource(id = R.string.now_playing_movies),
        color = colorResource(id = R.color.light_theme),
        fontWeight = FontWeight.Bold,
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        items(movies.size) { index ->
            val movie = movies[index]
            movie.let {
                GridMovieItem(
                    movie = it,
                ) {
                    val action = it.let { movieItem ->
                        MainFragmentDirections.actionMainFragmentToDetailFragment(
                            Constants.TOP_RATED,
                            movieItem.id!!,
                        )
                    }
                    navController.navigate(action)
                }
            }
        }
    }
}

@Composable
fun TopRatedMoviesList(navController: NavController, movies: List<MovieResult>) {
    Text(
        modifier = Modifier.padding(start = 10.dp, top = 50.dp),
        text = stringResource(id = R.string.top_rated_movies),
        color = colorResource(id = R.color.light_theme),
        fontWeight = FontWeight.Bold,
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        items(movies.size) { index ->
            val movie = movies[index]
            movie.let {
                GridMovieItem(
                    movie = it,
                ) {
                    val action = it.let { movieItem ->
                        MainFragmentDirections.actionMainFragmentToDetailFragment(
                            Constants.TOP_RATED,
                            movieItem.id!!,
                        )
                    }
                    navController.navigate(action)
                }
            }
        }
    }
}

@Composable
fun PopularMoviesList(
    navController: NavController,
    movies: List<MovieResult>,

) {
    val popularMovieViewModel: PopularMovieViewModel = viewModel()
    val favoriteMovieViewModel: FavoriteMovieViewModel = viewModel()
    popularMovieViewModel.observeMovieFromViewModelFavorite(favoriteMovieViewModel)
    val lazyListState = rememberLazyListState()
    val lazyGridState = rememberLazyGridState()
    val configuration = LocalConfiguration.current
    val gridCellCount = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            SPAN_COUNT_6
        }

        else -> {
            SPAN_COUNT_4
        }
    }
    var isGridMode by remember { mutableStateOf(false) }
    Row {
        Text(
            modifier = Modifier.padding(start = 10.dp, top = 10.dp),
            text = stringResource(id = R.string.popular_movies),
            color = colorResource(id = R.color.light_theme),
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { isGridMode = !isGridMode }) {
            Icon(
                imageVector = if (isGridMode) Icons.Filled.List else Icons.Filled.GridOn,
                contentDescription = null,
                tint = colorResource(id = R.color.light_theme),
            )
        }
    }

    if (isGridMode) {
        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Fixed(gridCellCount),
            modifier = Modifier
                .height(700.dp)
                .padding(bottom = 20.dp),
        ) {
            items(movies.size) { index ->
                val movie = movies[index]
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .padding(4.dp),
                ) {
                    GridMovieItem(
                        movie = movie,
                        onItemClick = {
                            val action =
                                MainFragmentDirections.actionMainFragmentToDetailFragment(
                                    Constants.TOP_RATED,
                                    movie.id ?: -1,
                                )
                            navController.navigate(action)
                        },
                    )
                    if (index == movies.size - 1) {
                        // Load next page when reaching the last item
                        popularMovieViewModel.getNextPage()
                    }
                }
            }
            val reachedEnd =
                lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == (lazyGridState.layoutInfo.totalItemsCount - 1)
            if (reachedEnd) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator() // Loading indicator
                    }
                }
            }
        }
    } else {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp)
                .padding(top = 10.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(movies.size) { index ->
                val movie = movies[index]
                movie.let {
                    PopularMovieItem(
                        movie = it,
                        onItemClick = {
                            val action = it.let { movieItem ->
                                MainFragmentDirections.actionMainFragmentToDetailFragment(
                                    Constants.TOP_RATED,
                                    movieItem.id!!,
                                )
                            }
                            navController.navigate(action)
                        },
                        favoriteMovieViewModel,
                    )

                    if (index == movies.size - 1) {
                        // Load next page when reaching the last item
                        popularMovieViewModel.getNextPage()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PopularMovieItem(
    movie: MovieResult?,
    onItemClick: () -> Unit,
    favoriteMovieViewModel: FavoriteMovieViewModel,
) {
    val isFavorite = movie?.isFavorite ?: false
    var isMovieFavorite by remember { mutableStateOf(isFavorite) }
    val favoriteIconTint: Int = if (isMovieFavorite) {
        R.color.red
    } else {
        R.color.light_theme
    }
    val favoriteIconRes = if (isMovieFavorite) {
        R.drawable.add_fav_filled_icon // Use the filled icon resource
    } else {
        R.drawable.add_fav_empty_icon // Use the empty icon resource
    }

    Card(
        onClick = onItemClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .background(colorResource(id = R.color.transparent)),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.light_bold_theme)),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.main_theme_bg))
                .border(
                    1.dp,
                    colorResource(id = R.color.light_bold_theme),
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlideImage(
                model = "https://image.tmdb.org/t/p/w500${movie?.posterPath}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp, 90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent),
            )
            Spacer(modifier = Modifier.width(10.dp))
            movie?.title?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .weight(2f)
                        .align(Alignment.CenterVertically),
                    color = colorResource(id = R.color.light_theme),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterVertically),
            ) {
                IconButton(
                    onClick = {
                        isMovieFavorite = !isMovieFavorite
                        val favMovie = FavoriteMovie(
                            0,
                            movie?.id,
                            movie?.title,
                            movie?.posterPath,
                            movie?.voteAverage,
                        )

                        favoriteMovieViewModel.actionFavButton(favMovie)
                    },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(34.dp),
                ) {
                    Icon(
                        painter = painterResource(id = favoriteIconRes),
                        contentDescription = null,
                        tint = colorResource(id = favoriteIconTint),
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.vote_star),
                        contentDescription = null,
                        tint = colorResource(id = R.color.light_theme),
                    )
                    Spacer(modifier = Modifier.width(5.dp))

                    Text(

                        text = movie?.voteAverage.toString(),
                        color = colorResource(id = R.color.light_theme),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GridMovieItem(
    movie: MovieResult?,
    onItemClick: () -> Unit,
) {
    val isFavorite = movie?.isFavorite ?: false
    var isMovieFavorite by remember { mutableStateOf(isFavorite) }

    val favoriteIconTint: Int = if (isMovieFavorite) {
        R.color.red
    } else {
        R.color.light_theme
    }
    val favoriteIconRes = if (isMovieFavorite) {
        R.drawable.add_fav_filled_icon // Use the filled icon resource
    } else {
        R.drawable.add_fav_empty_icon // Use the empty icon resource
    }

    val favoriteMovieViewModel: FavoriteMovieViewModel = viewModel()
    Card(
        onClick = onItemClick,
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(R.color.transparent)),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.light_bold_theme)),

    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.BottomEnd,
        ) {
            GlideImage(
                model = "https://image.tmdb.org/t/p/w500${movie?.posterPath}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp, 120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent),
            )
            IconButton(
                onClick = {
                    isMovieFavorite = !isMovieFavorite
                    val favMovie = FavoriteMovie(
                        0,
                        movie?.id,
                        movie?.title,
                        movie?.posterPath,
                        movie?.voteAverage,
                    )

                    favoriteMovieViewModel.actionFavButton(favMovie)
                },
                modifier = Modifier
                    .size(30.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(colorResource(R.color.transparent_white)),

            ) {
                Box {
                    Icon(
                        painter = painterResource(id = favoriteIconRes),
                        contentDescription = null,
                        tint = colorResource(id = favoriteIconTint),
                    )
                }
            }
        }
    }
}

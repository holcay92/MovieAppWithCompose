package com.example.movieapp.view.homePage

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieapp.Constants
import com.example.movieapp.R
import com.example.movieapp.model.movie.MovieResult
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.view.BaseFragment
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import com.example.movieapp.viewModel.NowPlayingMovieViewModel
import com.example.movieapp.viewModel.PopularMovieViewModel
import com.example.movieapp.viewModel.TopRatedMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment :
    BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // showProgressDialog()
        return ComposeView(requireContext()).apply {
            setContent {
                MainScreen(
                    navController = findNavController(),
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setup user interface
        setupViews()
        setupBackPressedCallback()
    }

    private fun setupBackPressedCallback() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                alertDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback,
        )
    }

    private fun setupViews() {
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.title = ""
        val isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        SPAN_COUNT = if (isLandscape) SPAN_COUNT_5 else SPAN_COUNT_3
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    companion object {
        private var SPAN_COUNT = 2
        private var SPAN_COUNT_1 = 1
        private var SPAN_COUNT_3 = 3
        private var SPAN_COUNT_5 = 5
        private var viewType = false
        private var page = 1
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.warning)
        builder.setMessage(R.string.quite_from_app__message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            requireActivity().finish() // Exit the app
        }
        builder.setNegativeButton(R.string.no) { _, _ ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}

@Composable
fun MainScreen(navController: NavController) {
    val popularMovieViewModel: PopularMovieViewModel = viewModel()
    val topRatedMovieViewModel: TopRatedMovieViewModel = viewModel()
    val nowPlayingMovieViewModel: NowPlayingMovieViewModel = viewModel()
    popularMovieViewModel.fetchPopularMovieList(1)
    topRatedMovieViewModel.fetchTopRatedMovieList()
    nowPlayingMovieViewModel.fetchNowPlayingMovies()
    val popularMovies = popularMovieViewModel.popularMovieResponse.observeAsState(emptyList())
    val topRatedMovies = topRatedMovieViewModel.tRMovieResponse.observeAsState(emptyList())
    val nowPlayingMovies = nowPlayingMovieViewModel.nowPlayingMovies.observeAsState(emptyList())
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

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PopularMovieItem(
    movie: MovieResult?,
    onItemClick: () -> Unit,
) {
    val popularMovieViewModel: PopularMovieViewModel = viewModel()
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
                        Log.d("TAGX", "actionFavoriteMovie: ${favMovie.id}")

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
    Log.d("TAGX", "isFavorite: $isFavorite")
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
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(R.color.transparent)),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.light_bold_theme)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
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
                    Log.d("TAGX", "actionFavoriteMovie: ${favMovie.id}")

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

@Composable
fun NowPlayingMoviesList(navController: NavController, movies: List<MovieResult>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
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
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
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
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(700.dp)
            .padding(top = 50.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(movies.size) { index ->
            val movie = movies[index]
            movie.let {
                PopularMovieItem(
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
        /* if (isLoading) {
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
         }*/
    }
}

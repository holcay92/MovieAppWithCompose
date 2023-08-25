package com.example.movieapp.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieapp.Constants
import com.example.movieapp.R
import com.example.movieapp.model.actor.Actor
import com.example.movieapp.model.actorMovies.ActorMoviesCrew
import com.example.movieapp.viewModel.ActorMoviesViewModel
import com.example.movieapp.viewModel.ActorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setupToolbar()
        val actorId = ActorFragmentArgs.fromBundle(requireArguments()).actorId
        return ComposeView(requireContext()).apply {
            setContent {
                ActorScreen(
                    actorId,
                    navController = findNavController(),
                )
            }
        }
    }

    private fun setupToolbar() { // todo: compose toolbar
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.setTitle(R.string.info)
        toolbar.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

@Composable
fun ActorScreen(actorId: Int, navController: NavController) {
    val actorViewModel: ActorViewModel = viewModel()
    val actorMoviesViewModel: ActorMoviesViewModel = viewModel()
    actorViewModel.getActorDetail(actorId)
    actorMoviesViewModel.getActorMovies(actorId)
    val actorResponse by actorViewModel.actorResponse.observeAsState()
    val actorMoviesResponse by actorMoviesViewModel.actorMovies.observeAsState()

    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current
    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }
    actorResponse?.name?.let {
        CustomTopAppBar(title = it) {
            navController.popBackStack()
        }
    }
    when (orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.transparent))
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    actorResponse?.let { ActorImage(it) }
                }
                item {
                    actorResponse?.let { ActorInfoSection(it, actorMoviesResponse, navController) }
                }
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.transparent))
                    .padding(top = 50.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        actorResponse?.let { ActorImage(it) }
                        actorResponse?.let {
                            ActorInfoSection(
                                it,
                                actorMoviesResponse,
                                navController,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ActorImage(actor: Actor) {
    Card(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .background(color = Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.light_bold_theme)),
        elevation = CardDefaults.cardElevation(10.dp),

    ) {
        GlideImage(
            model = "https://image.tmdb.org/t/p/w500${actor.profilePath}",
            contentDescription = null,
            modifier = Modifier
                .width(200.dp)
                .height(300.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Transparent),
        )
    }
}

@Composable
fun ActorInfoSection(
    actor: Actor,
    movieList: List<ActorMoviesCrew>?,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 8.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.actor_name),
                color = colorResource(id = R.color.light_theme),
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
            )
            Text(
                text = actor.name ?: "",
                color = colorResource(id = R.color.light_bold_theme),
                fontStyle = FontStyle.Italic,
                fontSize = 18.sp,
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.birthday),
                color = colorResource(id = R.color.light_theme),
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
            )
            Text(
                text = actor.birthday ?: "",
                color = colorResource(id = R.color.light_bold_theme),
                fontStyle = FontStyle.Italic,
                fontSize = 18.sp,
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.country),
                color = colorResource(id = R.color.light_theme),
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
            )
            Text(
                text = actor.placeOfBirth ?: "",
                color = colorResource(id = R.color.light_bold_theme),
                fontStyle = FontStyle.Italic,
                fontSize = 18.sp,
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(colorResource(id = R.color.light_transparent_theme)),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.movies),
                color = colorResource(id = R.color.light_theme),
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.transparent)),
            ) {
                movieList?.size?.let {
                    items(it) { index ->
                        val movie = movieList[index]
                        ActorMovieItem(
                            movie = movie,

                        ) {
                            movie.id.let { movieId ->
                                val action =
                                    ActorFragmentDirections.actionActorFragmentToDetailFragment(
                                        Constants.POPULAR,
                                        movieId,
                                    )
                                navController.navigate(action)
                            }
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(colorResource(id = R.color.light_transparent_theme)),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.biography),
                    color = colorResource(id = R.color.light_theme),
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 10.dp),
                )
                actor.biography?.let {
                    Text(
                        text = it,
                        color = colorResource(id = R.color.light_bold_theme),
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ActorMovieItem(
    movie: ActorMoviesCrew?,
    onItemClick: (ActorMoviesCrew) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.light_bold_theme)),
        onClick = {
            if (movie != null) {
                onItemClick(movie)
            }
        },

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            if (movie?.posterPath != null) {
                GlideImage(
                    model =
                    "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(90.dp)
                        .clip(RoundedCornerShape(10.dp)).background(Color.Red),

                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.blank_movie_image),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(90.dp)
                        .clip(RoundedCornerShape(10.dp)),
                )
            }
        }
    }
}

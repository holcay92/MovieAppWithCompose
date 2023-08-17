package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
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
}

@Composable
fun ActorScreen(actorId: Int, navController: NavController) {
    val actorViewModel: ActorViewModel = viewModel()
    val actorMoviesViewModel: ActorMoviesViewModel = viewModel()
    actorViewModel.getActorDetail(actorId)
    actorMoviesViewModel.getActorMovies(actorId)
    val actorResponse by actorViewModel.actorResponse.observeAsState()
    val actorMoviesResponse by actorMoviesViewModel.actorMovies.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .background(colorResource(id = R.color.transparent)),
    ) {
        actorResponse?.let { ActorCard(it) }
        DividerLine()
        actorResponse?.let { ActorInfoSection(it) }
        DividerLine()
        actorMoviesResponse?.let {
            ActorMoviesSection(actorMoviesResponse, navController) // todo
        }
        DividerLine()
        ActorBioSection()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ActorCard(actor: Actor) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp).background(color = Color.Green),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 10.dp,
        border = BorderStroke(1.dp, Color.Gray),
    ) {
        GlideImage(
            model = "https://image.tmdb.org/t/p/w500${actor.profilePath}",
            contentDescription = null,
            modifier = Modifier.width(240.dp).height(360.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Red),
        )
    }
}

@Composable
fun DividerLine() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(colorResource(id = com.example.movieapp.R.color.light_transparent_theme)),
    )
}

@Composable
fun ActorInfoSection(
    actor: Actor,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
    ) {
        Text(text = "Actor Name", color = colorResource(id = R.color.light_theme))
        Text(text = actor.name ?: "", color = colorResource(id = R.color.light_theme))

        // Repeat the pattern for other actor info fields (birthday, country, etc.)
    }
}

@Composable
fun ActorMoviesSection(
    movieList: List<ActorMoviesCrew>?,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
    ) {
        Text(text = "Movies", color = colorResource(id = R.color.light_theme))

        LazyRow(
            modifier = Modifier.fillMaxSize().padding(5.dp)
                .background(colorResource(id = R.color.transparent)),
        ) {
            movieList?.size?.let {
                items(it) { index ->
                    val movie = movieList[index]
                    ActorMovieItem(
                        movie = movie,

                    ) {
                        movie.id.let { movieId ->
                            navController.navigate("movieDetail/$movieId")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActorBioSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 8.dp),
    ) {
        Text(text = "Biography", color = colorResource(id = R.color.light_theme))
        Text(
            text = "OBSS OBSS OBSS OBSS OBSS OBSS OBSS OBSS OBSS OBSS",
            color = colorResource(id = R.color.light_theme),
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ActorMovieItem(
    movie: ActorMoviesCrew,
    onItemClick: (ActorMoviesCrew) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray),
        onClick = { onItemClick(movie) },

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            GlideImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = null,
                modifier = Modifier
                    .width(60.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(10.dp)),
            )
        }
    }
}

package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.* // ktlint-disable no-wildcard-imports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.* // ktlint-disable no-wildcard-imports
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.* // ktlint-disable no-wildcard-imports
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieapp.R
import com.example.movieapp.model.credits.Cast
import com.example.movieapp.model.credits.Credits
import com.example.movieapp.model.movieDetail.MovieDetail
import com.example.movieapp.model.movieImages.Poster
import com.example.movieapp.model.videos.VideoResult
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.viewModel.CreditsViewModel
import com.example.movieapp.viewModel.DetailFragmentMovieImageViewModel
import com.example.movieapp.viewModel.DetailViewModel
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val movieId = DetailFragmentArgs.fromBundle(requireArguments()).id
        return ComposeView(requireContext()).apply {
            setContent {
                DetailScreen(movieId, navController = findNavController())
            }
        }
    }
}

@Composable
fun DetailScreen(movieId: Int, navController: NavController) {
    val detailFragmentMovieImageViewModel: DetailFragmentMovieImageViewModel = viewModel()
    detailFragmentMovieImageViewModel.fetchMovieImageList(movieId)
    val movieImageList = detailFragmentMovieImageViewModel.imageResponse.observeAsState(emptyList())
    val detailViewModel: DetailViewModel = viewModel()
    val creditsViewModel: CreditsViewModel = viewModel()
    LaunchedEffect(movieId) {
        detailViewModel.fetchMovieDetail(movieId)
        creditsViewModel.getMovieCredits(movieId)
        detailViewModel.fetchMovieVideos(movieId)
    }

    val movieDetailResponse = detailViewModel.movieDetail.observeAsState()
    val creditsResponseList = creditsViewModel.creditsResponse.observeAsState()
    val movieTrailersResponse = detailViewModel.movieVideos.observeAsState()
    val loadingState by detailViewModel.loadingState.observeAsState()
    movieDetailResponse.value?.title?.let {
        CustomTopAppBar(
            it,
            onBackClick = { navController.popBackStack() },
        )
    }
    if (loadingState == true) {
        // Show loading indicator
        CircularProgressIndicator(modifier = Modifier.width(100.dp).height(100.dp))
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 50.dp),
        ) {
            item {
                movieImageList.value?.let { MovieImageLayout(it) }
            }
            item {
                ReviewAndFavoriteLayout(
                    navController,
                    movieDetailResponse,
                )
            }
            item {
                MovieDetailLayout(movieDetailResponse, creditsResponseList)
            }
            item {
                creditsResponseList.value?.cast?.let { actor ->
                    MovieActorLayout(actor) {
                        val action =
                            DetailFragmentDirections.actionDetailFragmentToActorFragment(it)
                        navController.navigate(action)
                    }
                }
            }
            item {
                movieTrailersResponse.value?.let { TrailerLayout(it, navController) }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieImageLayout(movieImageResponse: List<Poster>) {
    LazyRow(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(movieImageResponse.size) { imageResource ->
            val movieImageResponseItem = movieImageResponse[imageResource].filePath
            Card(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(R.color.transparent)),
                elevation = CardDefaults.cardElevation(5.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, colorResource(id = R.color.light_bold_theme)),
            ) {
                GlideImage(
                    model = "https://image.tmdb.org/t/p/w500$movieImageResponseItem",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(200.dp)
                        .height(300.dp),
                )
            }
        }
    }
}

@Composable
fun ReviewAndFavoriteLayout(
    navController: NavController,
    movieDetailResponse: State<MovieDetail?>,
) {
    val favoriteMovieViewModel: FavoriteMovieViewModel = viewModel()
    val movie = movieDetailResponse.value
    val favMovie =
        FavoriteMovie(0, movie?.id, movie?.title, movie?.posterPath, movie?.voteAverage)

    val favMovieList = favoriteMovieViewModel.favMovieList.observeAsState().value
    val isMovieFavorite = favMovieList?.any { it.id == movie?.id } == true

    val favoriteIconTint: Int = if (isMovieFavorite) {
        R.color.red
    } else {
        R.color.light_theme
    }
    val favoriteIconRes = if (isMovieFavorite) {
        R.drawable.add_fav_filled_icon
    } else {
        R.drawable.add_fav_empty_icon
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(
                onClick = {
                    val action =
                        movie?.id?.let {
                            DetailFragmentDirections.actionDetailFragmentToDetailReviewFragment(
                                it,
                            )
                        }
                    if (action != null) {
                        navController.navigate(action)
                    }
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Transparent),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.read_reviews_icon),
                    contentDescription = null,
                    tint = colorResource(id = R.color.light_theme),

                )
            }

            Text(
                text = stringResource(id = R.string.show_reviews),
                color = colorResource(id = R.color.light_theme),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 5.dp),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {
            favoriteMovieViewModel.actionFavButton(favMovie)
        }) {
            Icon(
                painter = painterResource(id = favoriteIconRes),
                contentDescription = null,
                tint = colorResource(id = favoriteIconTint),
                modifier = Modifier.size(40.dp),
            )
        }
    }
}

@Composable
fun MovieDetailLayout(movieDetail: State<MovieDetail?>, movieCrewDetail: State<Credits?>) {
    val director = movieCrewDetail.value?.crew?.find { it.job == "Director" }?.name
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(id = R.color.light_transparent_theme)), // divider line
        )
        Card(
            modifier = Modifier
                .padding(top = 16.dp)
                .background(colorResource(R.color.transparent)),
            elevation = CardDefaults.cardElevation(10.dp),
        ) {
            Row(
                modifier = Modifier
                    .background(colorResource(R.color.main_theme))
                    .padding(4.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.rating),
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.light_theme),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vote_star),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = movieDetail.value?.voteAverage.toString(), // vote average
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.light_bold_theme),
                    )
                    Text(
                        text = " /10  (",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_theme),
                    )
                    Text(
                        text = movieDetail.value?.voteCount.toString(),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_bold_theme), // vote count
                    )
                    Text(
                        text = " votes)",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_theme),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1f),
            ) {
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(10.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(R.color.main_theme))
                            .padding(4.dp),
                        text = stringResource(id = R.string.release_date),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_theme),
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(R.color.main_theme))
                            .padding(4.dp),
                        text = movieDetail.value?.releaseDate.toString(), // release date
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_bold_theme),
                    )
                }
            }

            Spacer(modifier = Modifier.width(1.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1f),
            ) {
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(10.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(R.color.main_theme))
                            .padding(4.dp),
                        text = stringResource(id = R.string.runtime),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_theme),
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(R.color.main_theme))
                            .padding(4.dp),
                        text = "${movieDetail.value?.runtime?.toString() ?: 0} min", // runtime
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_bold_theme),
                    )
                }
            }

            Spacer(modifier = Modifier.width(1.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1f),
            ) {
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(10.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(R.color.main_theme))
                            .padding(4.dp),
                        text = stringResource(id = R.string.budget),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.light_theme),

                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(R.color.main_theme))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.budget_icon),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = movieDetail.value?.budget.toString(), // budget
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.light_bold_theme),
                        )
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1f),
            ) {
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(10.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(R.color.main_theme))
                            .padding(4.dp),
                        text = stringResource(id = R.string.director), // director
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.light_theme),

                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(R.color.main_theme))
                            .padding(4.dp),
                        text = director.toString(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_bold_theme),
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1f),
            ) {
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(10.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(R.color.main_theme))
                            .padding(4.dp),
                        text = stringResource(id = R.string.genres), // genres
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.light_theme),

                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(R.color.main_theme)),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        items(movieDetail.value?.genres.orEmpty().size) { genre ->
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(colorResource(R.color.main_theme))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                text = movieDetail.value?.genres?.get(genre)?.name.toString(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = colorResource(id = R.color.light_bold_theme),
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(id = R.color.light_transparent_theme)), // divider line
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 16.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(R.color.main_theme))
                    .padding(4.dp),
                text = stringResource(id = R.string.overview), // overview
                color = colorResource(id = R.color.light_theme),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )

            Text(
                text = movieDetail.value?.overview.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 10.dp)
                    .padding(vertical = dimensionResource(id = R.dimen.detailFragmentCardViewDetailTextPaddingValue10dp)),
                color = colorResource(id = R.color.light_bold_theme),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Clip,
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MovieActorLayout(castResponse: List<Cast?>, onItemClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(id = R.color.light_transparent_theme)), // divider line
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(R.color.main_theme))
                    .padding(4.dp),
                text = stringResource(id = R.string.cast), // cast
                color = colorResource(id = R.color.light_theme),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(castResponse.size) { cast ->
                Column(
                    modifier = Modifier,

                ) {
                    Card(
                        onClick = { castResponse[cast]?.id?.let { onItemClick(it) } },
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(colorResource(R.color.transparent)),
                        elevation = CardDefaults.cardElevation(5.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, colorResource(id = R.color.light_bold_theme)),
                    ) {
                        val actorImage: Any = if (castResponse[cast]?.profilePath != null) {
                            "https://image.tmdb.org/t/p/w500${castResponse[cast]?.profilePath}"
                        } else {
                            R.drawable.empty_person
                        }
                        GlideImage(
                            model = actorImage,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(60.dp)
                                .height(90.dp),
                        )
                    }
                    Text(
                        modifier = Modifier
                            .width(60.dp),
                        text = castResponse[cast]?.name.toString(),
                        color = colorResource(id = R.color.light_bold_theme),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 10.sp,
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(id = R.color.light_transparent_theme)), // divider line
        )
    }
}

@Composable
fun TrailerLayout(movieTrailersResponse: List<VideoResult>, navController: NavController) {
    var videoNumber by remember { mutableIntStateOf(0) }
    val trailer = movieTrailersResponse.getOrNull(videoNumber)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        IconButton(
            onClick = {
                videoNumber = if (videoNumber == 0) {
                    movieTrailersResponse.size - 1 // cycle to the last video
                } else {
                    (videoNumber - 1) % movieTrailersResponse.size
                }
            },
            modifier = Modifier
                .size(30.dp)
                .weight(1f),
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp),
                painter = painterResource(id = R.drawable.previous_trailer_arrow),
                contentDescription = null,
                tint = colorResource(id = R.color.light_theme),
            )
        }
        Button(
            onClick = {
                val action =
                    trailer?.key?.let {
                        DetailFragmentDirections.actionDetailFragmentToVideoFullScreenActivity(
                            it,
                        )
                    }
                if (action != null) {
                    navController.navigate(action)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.main_theme)),
            modifier = Modifier
                .weight(2f)
                .width(150.dp)
                .height(40.dp),
            content = {
                Text(
                    text = "Toggle Fullscreen",
                    color = colorResource(id = R.color.light_theme),
                    fontSize = 12.sp,
                )
            },
        )
        IconButton(
            onClick = {
                videoNumber = (videoNumber + 1) % movieTrailersResponse.size
            },
            modifier = Modifier
                .size(30.dp)
                .weight(1f),
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp),
                painter = painterResource(id = R.drawable.next_trailer_arrow),
                contentDescription = null,
                tint = colorResource(id = R.color.light_theme),
            )
        }
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(id = R.color.light_transparent_theme)), // divider line
    )
    InitYoutubePlayer(
        videoId = trailer?.key ?: "",
        onReady = { youTubePlayer ->
            youTubePlayer.loadVideo(trailer?.key ?: "", 0f)
        },
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp)
            .height(1.dp)
            .background(colorResource(id = R.color.light_transparent_theme)), // divider line
    )
}

@Composable
fun InitYoutubePlayer(
    videoId: String,
    onReady: (YouTubePlayer) -> Unit,
) {
    val context = LocalContext.current
    val view = remember { YouTubePlayerView(context) }

    AndroidView(
        factory = { view },
        modifier = Modifier.fillMaxSize(),
        update = {
            view.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                    onReady(youTubePlayer)
                }
            })
        },
    )
}

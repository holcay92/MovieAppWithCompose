package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.* // ktlint-disable no-wildcard-imports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.viewModel.CreditsViewModel
import com.example.movieapp.viewModel.DetailFragmentMovieImageViewModel
import com.example.movieapp.viewModel.DetailViewModel
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment() {

    private lateinit var currentVideoId: String
    private var videoNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val movieId = extractMovieIdFromArguments()
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    DetailScreen(movieId, findNavController())
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackButtonNavigation()

        /*  setupViewPager()
          adjustViewPagerDimensions()
          observeMovieImageList(movieId)
          observeMovieDetailAndVideos(movieId)
          handleShowReviewsButton(movieId)
          observeFavoriteMovieList(movieId)*/
        // Enable the back button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*  bindingDetail.apply {
              fullscreenButton.setOnClickListener {
                  handleFullScreenButton()
              }
              nextButton.setOnClickListener {
                  switchToNextVideo()
              }
              previousButton.setOnClickListener {
                  switchToPreviousVideo()
              }
          }*/
    }

    private fun setupBackButtonNavigation() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = DetailFragmentDirections.actionDetailFragmentToMainFragment()
                findNavController().navigate(action)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback,
        )
    }

    private fun extractMovieIdFromArguments(): Int {
        return DetailFragmentArgs.fromBundle(requireArguments()).id
    }

    /* private fun adjustViewPagerDimensions() { // todo height
         val displayMetrics = DisplayMetrics()
         requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
         val screenHeight = displayMetrics.heightPixels
         val halfScreenHeight = screenHeight / 2
         bindingDetail.viewPager.layoutParams.height = halfScreenHeight

         val viewPager = bindingDetail.viewPager
         val layoutParams = viewPager.layoutParams as LinearLayout.LayoutParams
         // for the landscape mode
         val orientation = resources.configuration.orientation
         if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
             layoutParams.width = resources.displayMetrics.widthPixels / 2
         } else {
             layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
         }

         viewPager.layoutParams = layoutParams
     }*/

    private fun handleFullScreenButton() {
        val action = DetailFragmentDirections.actionDetailFragmentToVideoFullScreenActivity(
            currentVideoId,
        )
        findNavController().navigate(action)
    }

    /*private fun initFirstVideo(video: VideoResult) {
        currentVideoId = video.key
        val youTubePlayerView: YouTubePlayerView = bindingDetail.youtubePlayerView1
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(currentVideoId, 0f)
            }
        })
    }
*//*
    private fun switchToNextVideo() {
        val videos = detailViewModel.movieVideos.value
        if (!videos.isNullOrEmpty()) {
            videoNumber = (videoNumber + 1) % videos.size
            val nextVideo = videos[videoNumber]
            updateVideoUI(nextVideo)
        }
    }

    private fun switchToPreviousVideo() {
        val videos = detailViewModel.movieVideos.value
        if (!videos.isNullOrEmpty()) {
            if (videoNumber == 0) {
                videoNumber = videos.size // cycle to the last video
            }
            videoNumber = (videoNumber - 1) % videos.size
            val previousVideo = videos[videoNumber]
            updateVideoUI(previousVideo)
        }
    }

    private fun updateVideoUI(video: VideoResult) {
        currentVideoId = video.key
        val youTubePlayerView: YouTubePlayerView = bindingDetail.youtubePlayerView1
        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(currentVideoId, 0f)
            }
        })
    }*/
}

@Composable
fun DetailScreen(movieId: Int, navController: NavController) {
    val detailFragmentMovieImageViewModel: DetailFragmentMovieImageViewModel = viewModel()
    detailFragmentMovieImageViewModel.fetchMovieImageList(movieId)
    val movieImageList = detailFragmentMovieImageViewModel.imageResponse.observeAsState(emptyList())
    val detailViewModel: DetailViewModel = viewModel()
    val creditsViewModel: CreditsViewModel = viewModel()
    detailViewModel.fetchMovieDetail(movieId)
    creditsViewModel.getMovieCredits(movieId)
    val movieDetailResponse = detailViewModel.movieDetail.observeAsState()
    val creditsResponseList = creditsViewModel.creditsResponse.observeAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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
                    val action = DetailFragmentDirections.actionDetailFragmentToActorFragment(it)
                    navController.navigate(action)
                }
            }
        }
        item {
            TrailerLayout(navController)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieImageLayout(movieImageResponse: List<Poster>) {
    LazyRow(
        modifier = Modifier.padding(top = 50.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(movieImageResponse.size) { imageResource ->
            val movieImageResponseItem = movieImageResponse[imageResource].file_path
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
                        text = movieDetail.value?.voteAverage.toString(),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_theme),
                    )
                    Text(
                        text = " /10  (",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_theme),
                    )
                    Text(
                        text = movieDetail.value?.voteCount.toString(),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_theme),
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
                        text = movieDetail.value?.releaseDate.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_theme),
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
                        text = "${movieDetail.value?.runtime?.toString() ?: 0} min",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_theme),
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
                            text = movieDetail.value?.budget.toString(),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.light_theme),
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
                        text = stringResource(id = R.string.director),
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
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_theme),
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
                        text = stringResource(id = R.string.director),
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
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.light_theme),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.main_theme))
                    .padding(4.dp),
                text = stringResource(id = R.string.genres),
                fontSize = 14.sp,
                color = colorResource(id = R.color.light_theme),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Action",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.light_theme),
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text(
                    text = "Action",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.light_theme),
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text(
                    text = "Action",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.light_theme),
                )
            }
        }
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
                    .background(colorResource(R.color.main_theme))
                    .padding(4.dp),
                text = stringResource(id = R.string.overview),
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
                        color = colorResource(id = R.color.light_theme),
                        fontSize = 10.sp,
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
fun TrailerLayout(navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        IconButton(
            onClick = { /* Handle next button click */ },
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
                /*   val action =
                       DetailFragmentDirections.actionDetailFragmentToVideoFullScreenActivity(videoKey)
                   navController.navigate(action)*/
            },
            modifier = Modifier.weight(1f),
            content = { Text(text = "Toggle Fullscreen") },
        )
        IconButton(
            onClick = { /* Handle next button click */ },
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
            .height(2.dp)
            .background(Color.LightGray),
    )

    // Replace with your YouTubePlayerView
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp)
            .background(Color.Gray),
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color.LightGray),
    )
}

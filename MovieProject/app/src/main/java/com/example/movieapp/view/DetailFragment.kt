package com.example.movieapp.view

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentDetailBinding
import com.example.movieapp.model.credits.Cast
import com.example.movieapp.model.credits.Crew
import com.example.movieapp.model.movieDetail.MovieDetail
import com.example.movieapp.model.videos.VideoResult
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.utils.ZoomOutPageTransformer
import com.example.movieapp.view.adapters.ActorAdapter
import com.example.movieapp.view.adapters.MovieImageAdapter
import com.example.movieapp.viewModel.CreditsViewModel
import com.example.movieapp.viewModel.DetailFragmentMovieImageViewModel
import com.example.movieapp.viewModel.DetailViewModel
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment() {

    private lateinit var bindingDetail: FragmentDetailBinding
    private val detailFragmentMovieImageViewModel by viewModels<DetailFragmentMovieImageViewModel>()
    private val detailViewModel by viewModels<DetailViewModel>()
    private val favoriteMovieViewModel by viewModels<FavoriteMovieViewModel>()
    private val creditsViewModel by viewModels<CreditsViewModel>()
    private lateinit var movieImageAdapter: MovieImageAdapter
    private lateinit var actorAdapter: ActorAdapter
    private lateinit var currentVideoId: String
    private var videoNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindingDetail = FragmentDetailBinding.inflate(inflater, container, false)
        return bindingDetail.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackButtonNavigation()
        val movieId = extractMovieIdFromArguments()
        setupViewPager()
        adjustViewPagerDimensions()
        observeMovieImageList(movieId)
        observeMovieDetailAndVideos(movieId)
        handleShowReviewsButton(movieId)
        observeFavoriteMovieList(movieId)
        // Enable the back button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bindingDetail.apply {
            fullscreenButton.setOnClickListener {
                handleFullScreenButton()
            }
            nextButton.setOnClickListener {
                switchToNextVideo()
            }
            previousButton.setOnClickListener {
                switchToPreviousVideo()
            }
        }
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

    private fun setupViewPager() {
        movieImageAdapter = MovieImageAdapter()
        bindingDetail.viewPager.adapter = movieImageAdapter
        bindingDetail.viewPager.setPageTransformer(ZoomOutPageTransformer())
    }

    private fun adjustViewPagerDimensions() {
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
    }

    private fun observeMovieImageList(movieId: Int) {
        detailFragmentMovieImageViewModel.fetchMovieImageList(movieId)
        detailFragmentMovieImageViewModel.imageResponse.observe(viewLifecycleOwner) {
            movieImageAdapter.updateList(it)
        }
    }

    private fun observeMovieDetailAndVideos(movieId: Int) {
        showProgressDialog()
        detailViewModel.fetchMovieDetail(movieId)
        detailViewModel.fetchMovieVideos(movieId)
        creditsViewModel.getMovieCredits(movieId)
        detailViewModel.movieDetail.observe(viewLifecycleOwner) {
            if (it != null) {
                hideProgressDialog()
                updateUI(it)
            }
        }
        detailViewModel.movieVideos.observe(viewLifecycleOwner) { it ->
            if (!it.isNullOrEmpty()) {
                hideProgressDialog()
                initFirstVideo(it[0])
            }
        }
        creditsViewModel.creditsResponse.observe(viewLifecycleOwner) { credits ->
            hideProgressDialog()

            if (credits != null) {
                actorAdapter = ActorAdapter(object : ActorAdapter.OnItemClickListener {
                    override fun onItemClick(actor: Cast) {
                        val action =
                            actor.id?.let {
                                DetailFragmentDirections.actionDetailFragmentToActorFragment(
                                    it,
                                )
                            }
                        if (action != null) {
                            findNavController().navigate(action)
                        }
                    }
                })

                bindingDetail.actorRv.adapter = actorAdapter
                actorAdapter.updateList(credits.cast)
                updateDirector(credits.crew)
            }
        }

        detailViewModel.errorMessageMovieDetail.observe(viewLifecycleOwner) {
            hideProgressDialog()
            showErrorDialog(it)
        }
        detailViewModel.errorMessageMovieVideos.observe(viewLifecycleOwner) {
            hideProgressDialog()
            showErrorDialog(it)
        }
    }

    private fun handleShowReviewsButton(movieId: Int) {
        bindingDetail.showReviews.setOnClickListener {
            val action =
                DetailFragmentDirections.actionDetailFragmentToDetailReviewFragment(movieId)
            findNavController().navigate(action)
        }
    }

    private fun observeFavoriteMovieList(movieId: Int) {
        favoriteMovieViewModel.favMovieList.observe(viewLifecycleOwner) { favoriteMovies ->
            val isFav = favoriteMovies.any { it.id == movieId }
            updateFavButtonState(isFav)
            bindingDetail.favButton.setOnClickListener {
                val movie = detailViewModel.movieDetail.value
                val favMovie =
                    FavoriteMovie(0, movieId, movie?.title, movie?.posterPath, movie?.voteAverage)
                favoriteMovieViewModel.actionFavButton(favMovie)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(movieDetail: MovieDetail?) {
        bindingDetail.apply {
            movieDetail?.let {
                // movieTitle?.text = it.title
                releaseDateValue.text = it.releaseDate?.take(4)
                movieOverview.text = it.overview
                ratingValue.text = it.voteAverage.toString()
                bindingDetail.budgetValue.text = if (it.budget == 0) {
                    getString(R.string.not_available)
                } else {
                    it.budget.toString()
                }
                ratingCount.text = it.voteCount.toString()
                runtimeValue.text = "${movieDetail.runtime.toInt()} min"
                val genreViews =
                    listOf(bindingDetail.genre1, bindingDetail.genre2, bindingDetail.genre3)
                val genres = movieDetail.genres

                genreViews.forEachIndexed { index, genreView ->
                    if (genres != null) {
                        if (index < genres.size) {
                            genreView.text = genres[index].name
                        }
                    }

                    val toolbar = activity as AppCompatActivity
                    toolbar.supportActionBar?.title = it.title
                }
            }
        }
    }

    private fun updateFavButtonState(isFav: Boolean) {
        val drawableResId =
            if (isFav) R.drawable.add_fav_filled_icon else R.drawable.add_fav_empty_icon
        bindingDetail.favButton.setImageResource(drawableResId)
    }

    private fun handleFullScreenButton() {
        val action = DetailFragmentDirections.actionDetailFragmentToVideoFullScreenActivity(
            currentVideoId,
        )
        findNavController().navigate(action)
    }

    private fun initFirstVideo(video: VideoResult) {
        currentVideoId = video.key
        val youTubePlayerView: YouTubePlayerView = bindingDetail.youtubePlayerView1
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(currentVideoId, 0f)
            }
        })
    }

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
    }

    private fun updateDirector(crew: List<Crew>?) {
        if (crew != null) {
            val director = crew.find { it.job == "Director" }
            if (director != null) {
                bindingDetail.directorValue?.text = director.name
            }
        }
    }
}

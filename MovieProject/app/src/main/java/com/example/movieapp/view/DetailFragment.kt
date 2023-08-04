package com.example.movieapp.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentDetailBinding
import com.example.movieapp.model.movieDetail.MovieDetail
import com.example.movieapp.model.videos.VideoResult
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.utils.ZoomOutPageTransformer
import com.example.movieapp.view.adapters.MovieImageAdapter
import com.example.movieapp.viewModel.DetailFragmentMovieImageViewModel
import com.example.movieapp.viewModel.DetailViewModel
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var bindingDetail: FragmentDetailBinding
    private val viewModelForImage by viewModels<DetailFragmentMovieImageViewModel>()
    private val viewModelForDetail by viewModels<DetailViewModel>()
    private val viewModelForFavorite by viewModels<FavoriteMovieViewModel>()
    private lateinit var adapter: MovieImageAdapter
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
        adjustViewPagerHeight()
        observeMovieImageList(movieId)
        observeMovieDetailAndVideos(movieId)
        setupShowReviewsButton(movieId)
        observeFavoriteMovieList(movieId)
        // Enable the back button
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bindingDetail.fullscreenButton.setOnClickListener {
            handleFullScreenButton()
        }
        bindingDetail.nextButton!!.setOnClickListener {
            switchToNextVideo()
        }
        bindingDetail.previousButton!!.setOnClickListener {
            switchToPreviousVideo()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("TAG_X", "onOptionsItemSelected: ")
        when (item.itemId) {
            android.R.id.home -> {
                Log.d("TAG_X", "onOptionsItemSelected: home")

                findNavController().navigateUp()
                return true
            }
            // Add other menu item handling here if needed
        }
        return super.onOptionsItemSelected(item)
    }

    private fun extractMovieIdFromArguments(): Int {
        return DetailFragmentArgs.fromBundle(requireArguments()).id
    }

    private fun setupViewPager() {
        adapter = MovieImageAdapter()
        bindingDetail.viewPager.adapter = adapter
        bindingDetail.viewPager.setPageTransformer(ZoomOutPageTransformer())
    }

    private fun adjustViewPagerHeight() {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val halfScreenHeight = screenHeight / 2
        bindingDetail.viewPager.layoutParams.height = halfScreenHeight
    }

    private fun observeMovieImageList(movieId: Int) {
        viewModelForImage.fetchMovieImageList(movieId)
        viewModelForImage.imageResponse.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }

    private fun observeMovieDetailAndVideos(movieId: Int) {
        viewModelForDetail.fetchMovieDetail(movieId)
        viewModelForDetail.fetchMovieVideos(movieId)
        viewModelForDetail.movieDetail.observe(viewLifecycleOwner) {
            updateUI(it)
        }
        runBlocking {
            viewModelForDetail.movieVideos.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (it.isNotEmpty()) {
                        initFirstVideo(it[0])
                    }
                }
            }
        }
    }

    private fun setupShowReviewsButton(movieId: Int) {
        bindingDetail.showReviews.setOnClickListener {
            val action =
                DetailFragmentDirections.actionDetailFragmentToDetailReviewFragment(movieId)
            findNavController().navigate(action)
        }
    }

    private fun observeFavoriteMovieList(movieId: Int) {
        viewModelForFavorite.favMovieList.observe(viewLifecycleOwner) { favoriteMovies ->
            val isFav = favoriteMovies.any { it.id == movieId }
            updateFavButtonState(isFav)
            bindingDetail.favButton.setOnClickListener {
                val movie = viewModelForDetail.movieDetail.value
                val favMovie = FavoriteMovie(0, movieId, movie?.title, movie?.poster_path)
                viewModelForFavorite.actionFavButton(favMovie)
            }
        }
    }

    private fun updateUI(movieDetail: MovieDetail?) {
        bindingDetail.apply {
            movieDetail?.let {
                movieTitle.text = it.title
                movieReleaseDate.text = it.release_date
                movieOverview.text = it.overview
                movieVote.text = it.vote_average.toString()
                movieBudget.text = it.budget.toString()
                movieAdult.text = if (it.adult!!) "Yes" else "No"
                movieVoteCount.text = it.vote_count.toString()
                val toolbar = activity as AppCompatActivity
                toolbar.supportActionBar?.title = it.title
            }
        }
    }

    private fun updateFavButtonState(isFav: Boolean) {
        val drawableResId =
            if (isFav) R.drawable.add_fav_filled_icon else R.drawable.add_fav_empty_icon
        bindingDetail.favButton.setBackgroundResource(drawableResId)
    }

    private fun handleFullScreenButton() {
        currentVideoId = currentVideoId
        val action = DetailFragmentDirections.actionDetailFragmentToVideoFullScreenActivity(
            currentVideoId,
        )
        findNavController().navigate(action)
    }

    private fun initFirstVideo(video: VideoResult) {
        currentVideoId = video.key
        Log.d("TAG_X", "initFirstVideo: $currentVideoId")
        val youTubePlayerView: YouTubePlayerView? = bindingDetail.youtubePlayerView1
        lifecycle.addObserver(youTubePlayerView!!)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(currentVideoId, 0f)
            }
        })
    }

    private fun switchToNextVideo() {
        val videos = viewModelForDetail.movieVideos.value
        if (!videos.isNullOrEmpty()) {
            // Increment the currentVideoIndex
            videoNumber = (videoNumber + 1) % videos.size
            Log.d("TAG_X", "switchToNextVideo: $videoNumber")
            // Get the next video
            val nextVideo = videos[videoNumber]
            // Update the UI with the details of the next video
            updateVideoUI(nextVideo)
        }
    }

    private fun switchToPreviousVideo() {
        val videos = viewModelForDetail.movieVideos.value
        if (!videos.isNullOrEmpty()) {
            // Decrement the currentVideoIndex
            if (videoNumber == 0) {
                videoNumber = videos.size
            }
            videoNumber = (videoNumber - 1) % videos.size
            Log.d("TAG_X", "switchToPreviousVideo: $videoNumber")
            // Get the previous video

            val previousVideo = videos[videoNumber]
            // Update the UI with the details of the previous video
            updateVideoUI(previousVideo)
        }
    }

    private fun updateVideoUI(video: VideoResult) {
        currentVideoId = video.key
        Log.d("TAG_X", "updateVideoUI: $currentVideoId")
        // Get the existing YouTubePlayer instance
        val youTubePlayerView: YouTubePlayerView? = bindingDetail.youtubePlayerView1
        youTubePlayerView?.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                // Load the new video in the existing player
                youTubePlayer.loadVideo(currentVideoId, 0f)
            }
        })
    }
}

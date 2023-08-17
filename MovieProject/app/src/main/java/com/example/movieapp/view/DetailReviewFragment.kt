package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieapp.R
import com.example.movieapp.model.review.ReviewResult
import com.example.movieapp.viewModel.DetailReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailReviewFragment : BaseFragment() {
    private val detailReviewViewModel by viewModels<DetailReviewViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val movieId = DetailReviewFragmentArgs.fromBundle(requireArguments()).movieId
        return ComposeView(requireContext()).apply {
            setContent {
                DetailReviewScreen(movieId)
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = activity as AppCompatActivity
        toolbar.supportActionBar?.setTitle(R.string.show_reviews)
    }
}

@Composable
fun DetailReviewScreen(movieId: Int) {
    val detailReviewViewModel: DetailReviewViewModel = viewModel()
    detailReviewViewModel.getReview(movieId)
    val reviewList = detailReviewViewModel.reviewList.collectAsState(initial = emptyList()).value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 35.dp, bottom = 10.dp),
    ) {
        if (reviewList != null) {
            if (reviewList.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_reviews_found),
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                DetailReviewAdapter(reviewList)
            }
        }
    }
}

@Composable
fun DetailReviewItem(review: ReviewResult) {
    Column(
        modifier = Modifier
            .padding(16.dp),
    ) {
        Text(text = review.author)
        Text(text = review.content)
        Text(text = review.authorDetails.rating.toString())
    }
}

@Composable
fun DetailReviewAdapter(reviewList: List<ReviewResult>) {
    LazyColumn {
        items(reviewList) { review ->
            DetailReviewItem(review)
        }
    }
}

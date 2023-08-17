package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
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
                    color = colorResource(id = R.color.light_theme),
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
        modifier = Modifier.border(
            width = 1.dp,
            color = colorResource(id = R.color.light_bold_theme),
            shape = MaterialTheme.shapes.medium,
        )
            .background(color = colorResource(id = R.color.main_theme_bg)).padding(16.dp),
    ) {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.author),
                color = colorResource(id = R.color.light_theme),
            )
            Spacer(modifier = Modifier)
            Text(
                text = review.author,
                modifier = Modifier,
                color = colorResource(id = R.color.light_theme),
            )
        }
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.author_vote),
                color = colorResource(id = R.color.light_theme),
            )
            Text(
                text = review.authorDetails.rating.toString(),
                color = colorResource(id = R.color.light_theme),
            )
        }
        Row(
            modifier = Modifier.fillMaxSize().padding(top = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = R.string.review),
                color = colorResource(id = R.color.light_theme),
            )
            Text(text = review.content, color = colorResource(id = R.color.light_theme))
        }
    }
}

@Composable
fun DetailReviewAdapter(reviewList: List<ReviewResult>) {
    LazyColumn {
        items(reviewList) { review ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp,
                ),
            ) {
                DetailReviewItem(review)
            }
        }
    }
}

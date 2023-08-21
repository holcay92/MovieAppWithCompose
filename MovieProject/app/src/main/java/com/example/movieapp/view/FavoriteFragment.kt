package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieapp.Constants
import com.example.movieapp.R
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FavoriteScreen(
                    navController = findNavController(this@FavoriteFragment),
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() { // todo compose transform
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setTitle(R.string.favorites)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

@Composable
fun FavoriteScreen(navController: NavController) {
    val favoriteMovieViewModel: FavoriteMovieViewModel = viewModel()
    val favoriteMovies = favoriteMovieViewModel.favMovieList.observeAsState(emptyList())
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 35.dp, bottom = 10.dp)
            .background(colorResource(id = R.color.main_theme)),
    ) {
        if (favoriteMovies.value.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empty_fav_list_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.empty_fav_list_text),
                    color = colorResource(id = R.color.light_theme),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            }
        } else {
            FavoriteMovieRecyclerView(
                favMovieList = favoriteMovies,
                onItemClick = {
                    val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                        Constants.POPULAR,
                        it.id!!,
                    )
                    navController.navigate(action)
                },
            )
        }
    }
}

@Composable
fun ShowRemoveConfirmationDialog(
    favoriteMovieViewModel: FavoriteMovieViewModel,
    movie: FavoriteMovie,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.warning)) },
        text = { Text(text = stringResource(id = R.string.remove_fav_movie_message)) },
        confirmButton = {
            Button(
                onClick = {
                    favoriteMovieViewModel.actionFavButton(movie)
                    onDismiss()
                },
            ) {
                Text(text = stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    // close the dialog without doing anything
                    onDismiss()
                },
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
    )
}

@Composable
fun FavoriteMovieRecyclerView(
    favMovieList: State<List<FavoriteMovie>>,
    onItemClick: (FavoriteMovie) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(favMovieList.value.size) { index ->
            val movie = favMovieList.value[index]
            FavoriteMovieItem(
                movie = movie,
                onItemClick = onItemClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun FavoriteMovieItem(
    movie: FavoriteMovie,
    onItemClick: (FavoriteMovie) -> Unit,
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(8.dp).background(Color.Transparent),
        shape = RoundedCornerShape(10.dp),

        border = BorderStroke(0.5.dp, colorResource(id = R.color.light_bold_theme)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),
        onClick = { onItemClick(movie) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().background(colorResource(R.color.movie_item_color_popular)),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            GlideImage(
                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                contentDescription = null,
                modifier = Modifier.width(60.dp).height(90.dp)
                    .clip(RoundedCornerShape(10.dp)),
            )
            Text(
                text = movie.title ?: "",
                modifier = Modifier.weight(1f).padding(start = 10.dp, end = 10.dp),
                color = colorResource(id = R.color.light_theme),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            IconButton(
                onClick = { isDialogVisible = true },
                modifier = Modifier.size(44.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_fav_filled_icon),
                    contentDescription = null,
                    modifier = Modifier.size(34.dp),
                    tint = colorResource(R.color.red),
                )
            }
            if (isDialogVisible) {
                ShowRemoveConfirmationDialog(
                    favoriteMovieViewModel = viewModel(),
                    movie = movie,
                    onDismiss = { isDialogVisible = false },
                )
            }
        }
    }
}

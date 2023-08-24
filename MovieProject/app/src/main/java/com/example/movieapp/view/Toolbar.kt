package com.example.movieapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.R

@Composable
fun CustomTopAppBar(
    title: String,
    onBackClick: () -> Unit,
) {
    val logo: Painter = painterResource(id = R.drawable.tmdb_icon)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.main_theme_bg))
            .height(56.dp).padding(top = 4.dp),

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = onBackClick, modifier = Modifier.weight(1f)) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back",
                    tint = colorResource(
                        id = R.color.light_theme,
                    ),
                )
            }

            Text(
                text = title,
                color = colorResource(id = R.color.light_theme),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),

            )

            Image(
                painter = logo,
                contentDescription = "Logo",
                modifier = Modifier.size(36.dp).weight(1f),
            )
        }
    }
}

@Composable
fun MainTopAppBar() {
    val logo: Painter = painterResource(id = R.drawable.tmdb_icon)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.main_theme_bg))
            .height(56.dp).padding(top = 8.dp),

    ) {
        Image(
            painter = logo,
            contentDescription = "Logo",
            modifier = Modifier.size(36.dp).align(Alignment.TopCenter),

        )
    }
}

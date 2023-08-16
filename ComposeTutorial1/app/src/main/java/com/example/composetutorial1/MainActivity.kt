package com.example.composetutorial1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageCard(Message("Halil", "Jetpack Compose at OBSS"))
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    // Add padding around our message
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(all = 16.dp).background(Color.White)
            .border(1.dp, Color.Red).fillMaxSize(),

    ) {
        Image(
            painter = painterResource(R.drawable.image1),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp),
        )

        // Add a horizontal space between the image and the column
        Spacer(modifier = Modifier.width(10.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("hello ${msg.author}!")
            // Add a vertical space between the author and message texts
            Spacer(modifier = Modifier.height(10.dp))
            Text(msg.body)
        }
        Box {
            Text(
                "Box",
                modifier = Modifier.width(50.dp).height(30.dp).background(Color.Red)
                    .border(1.dp, Color.Black),
            )
            Text(
                "Big Box",
                modifier = Modifier.width(100.dp).background(Color.Green).clip(shape = CircleShape),
            )
        }
    }
}

@Preview
@Composable
fun PreviewMessageCard() {
    MessageCard(
        msg = Message("Halil", "Hey, that's great!"),
    )
}

package com.example.composetutorial1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(Color.LightGray).fillMaxSize().padding(5.dp)
    ) {
        items(20) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                    .border(1.dp, Color.Black),) {
                Text("hello ${msg.author}!")
                // Add a vertical space between the author and message texts
                Spacer(modifier = Modifier.height(10.dp))
                Text(msg.body)
            }
            Box {
                Text(
                    "Box",
                    modifier = Modifier
                        .width(50.dp)
                        .height(30.dp)
                        .background(Color.Red)
                        .border(1.dp, Color.Black)
                        .align(Alignment.BottomEnd),
                )
                Text(
                    "Big Box",
                    modifier = Modifier
                        .width(100.dp)
                        .background(Color.Green)
                        .clip(shape = CircleShape)
                        .align(
                            Alignment.TopStart,
                        ),
                )
            }
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

package com.example.composetutorial1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {

    // private val mainViewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel = viewModel<MainViewModel>()
            var name by remember {
                mutableStateOf("")
            }
            var names by remember {
                mutableStateOf(listOf<String>())
            }
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = mainViewModel.backgroundColor,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),

                    ) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = name,
                            onValueChange = { text ->
                                name = text
                            },
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = {
                            if (name.isNotEmpty()) {
                                names = names + name
                            }
                            // empty text field
                            name = ""

                            mainViewModel.changeBackgroundColor()
                            Intent(this@MainActivity, SecondActivity::class.java).also {
                                startActivity(it)
                            }
                        }) {
                            Text(text = "Add")
                        }
                    }
                    NameList(names = names)
                }
            }
        }
    }

    @Composable
    fun NameList(names: List<String>, modifier: Modifier = Modifier) {
        LazyColumn(modifier) {
            items(names) { currentName ->
                Text(
                    text = currentName,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                )
                Divider()
            }
        }
    }
}

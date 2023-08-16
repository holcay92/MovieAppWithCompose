package com.example.composetutorial1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var backgroundColor by mutableStateOf(Color.White)
        private set

    fun changeBackgroundColor() {
        backgroundColor = if (backgroundColor == Color.White) {
            Color.Red
        } else {
            Color.White
        }
    }
}

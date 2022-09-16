package com.example.jetpack_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpack_compose.domain.util.TodoAndroidViewModelFactory
import com.example.jetpack_compose.ui.main.MainScreen
import com.example.jetpack_compose.ui.main.MainViewModel

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel(
                factory = TodoAndroidViewModelFactory(application)
            )
            MainScreen(viewModel = viewModel)
        }
    }
}

package com.example.files

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.files.compose.MainScreen
import com.example.files.ui.theme.FilesTheme

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilesTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(mainActivityViewModel)
                }
            }
        }
    }
}

/*
@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FilesTheme {
        MainScreen()
    }
}*/

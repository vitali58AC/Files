package com.example.files

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.rememberNavController
import com.example.files.compose.MainScreen
import com.example.files.compose.NavigationComponent
import com.example.files.custom_content_provider.CustomContentViewModel
import com.example.files.ui.theme.FilesTheme

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private val customContentViewModel: CustomContentViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel.firstLaunchDownload(applicationContext)
        customContentViewModel.saveDefaultTables()
        setContent {
            FilesTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavigationComponent(
                        navController = navController,
                        viewModel = mainActivityViewModel,
                        context = this,
                        customContentViewModel = customContentViewModel
                    )
                }
            }
        }
    }
}

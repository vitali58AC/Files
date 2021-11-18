package com.example.files.compose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.files.MainActivityViewModel

@Composable
fun MainScreen(viewModel: MainActivityViewModel) {
    val currentText = viewModel.url.value
    val active = viewModel.active.value
    val isLoading = viewModel.isLoading.value
    Column {
        LogoImage()
        Spacer(modifier = Modifier.height(60.dp))
        CustomText(text = "Enter link to download file:")
        EditText(
            active = active,
            label = "URL",
            currentText = currentText
        ) { viewModel.updateUrl(it) }
        DownloadButton(viewModel)
        ProgressIndicator(visibility = isLoading)
    }
}






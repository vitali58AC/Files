package com.example.files.compose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.files.MainActivityViewModel
import com.example.files.R

@Composable
fun MainScreen(viewModel: MainActivityViewModel, onCustomContentClick: () -> Unit) {
    val currentText = viewModel.url.value
    val active = viewModel.active.value
    val isLoading = viewModel.isLoading.value
    val downloadStatus by viewModel.downloadStatus.observeAsState(false)
    val context = LocalContext.current
    Column {
        LogoImage(viewModel)
        Spacer(modifier = Modifier.height(60.dp))
        CustomText(text = "Enter link to download file:")
        EditText(
            active = active,
            label = "URL",
            currentText = currentText
        ) { viewModel.updateUrl(it) }
        DownloadButton(
            text = stringResource(R.string.download),
            onCLick = {
                viewModel.getFileFromUrl(viewModel.url.value, context)
            },
            enabled = active
        )
        DownloadButton(
            text = "Custom content provider",
            onCLick = { onCustomContentClick() }
        )
        if (downloadStatus) {
            DownloadStatusToast(viewModel.fileName.value, context, viewModel)
        }
        ProgressIndicator(visibility = isLoading)
    }
}






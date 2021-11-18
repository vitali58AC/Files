package com.example.files.compose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.files.MainActivityViewModel
import com.example.files.R
import com.example.files.ui.theme.imageColor40

@Composable
fun DownloadButton(viewModel: MainActivityViewModel) {
    val context = LocalContext.current
    val downloadStatus by viewModel.downloadStatus.observeAsState(false)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = {
                viewModel.getFileFromUrl(viewModel.url.value, context)
            },
            colors = ButtonDefaults.buttonColors(
                imageColor40
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(6.dp),
            enabled = viewModel.active.value
        ) {
            Text(
                text = "Download",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.W600
            )

        }
        if (downloadStatus) {
            DownloadStatusToast(viewModel.fileName.value, context, viewModel)
        }
    }
}


@Composable
fun DownloadStatusToast(message: String, context: Context, viewModel: MainActivityViewModel) {
    val toast = { m: String -> Toast.makeText(context, m, Toast.LENGTH_SHORT).show() }
    val unSuccessMessage = stringResource(id = R.string.un_success_message)
    val successMessage = stringResource(id = R.string.success_message, formatArgs = arrayOf(message))
    val errorMessage = stringResource(id = R.string.error_message, formatArgs = arrayOf(message))
    ShowToast {
        when {
            message == "" -> toast(unSuccessMessage)
            !message.contains("HttpException") -> toast(successMessage)
            else -> toast(errorMessage)
        }
    }
    viewModel.setDownloadToFalse()
}
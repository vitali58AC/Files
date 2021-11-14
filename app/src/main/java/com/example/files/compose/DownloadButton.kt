package com.example.files.compose

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.files.MainActivityViewModel
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
            Toast.makeText(context, "File: ${viewModel.fileName.value} success downloaded", Toast.LENGTH_SHORT).show()
        }
    }
}
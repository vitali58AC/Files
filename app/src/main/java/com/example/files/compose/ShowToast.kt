package com.example.files.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun ShowToast(toast: () -> Unit) {
    val currentCall by rememberUpdatedState(toast)

    LaunchedEffect(true) {
        currentCall()
    }
}

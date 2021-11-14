package com.example.files.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.files.ui.theme.imageColor120

@Composable
fun CustomText(text: String) {
    Column(modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = imageColor120)
    }
}

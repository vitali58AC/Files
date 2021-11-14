package com.example.files.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.files.MainActivityViewModel
import com.example.files.ui.theme.imageColor80

@Composable
fun EditText(
    active: Boolean,
    label: String,
    currentText: String,
    onTextChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = currentText,
        onValueChange = onTextChange,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        maxLines = 2,
        modifier = Modifier
            .fillMaxWidth()
            .padding(9.dp),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = imageColor80,
            focusedLabelColor = imageColor80
        ),
        enabled = active
    )
}
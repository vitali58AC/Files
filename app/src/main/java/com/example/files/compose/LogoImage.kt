package com.example.files.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import com.example.files.MainActivityViewModel
import com.example.files.R

@Composable
fun LogoImage(viewModel: MainActivityViewModel) {
    val image =
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
           // bitmap = BitmapPainter(),
            painter = painterResource(id = R.drawable.download1),
            contentDescription = stringResource(R.string.user_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(240.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape,
                    clip = true
                )
        )
/*        Image(
            bitmap = viewModel.bitmap.asImageBitmap(),
            contentDescription = stringResource(R.string.user_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(240.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape,
                    clip = true
                )
        )*/
    }
}
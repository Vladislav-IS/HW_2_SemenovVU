package com.semenov.hw2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil3.compose.AsyncImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.constraintlayout.utils.widget.ImageFilterView
import coil3.request.ImageRequest

const val URL = "https://github.com/Vladislav-IS/HW_2_SemenovVU/blob/main/qr.png"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoadingImageFromInternetCoil()
        }
    }
}

@Preview
@Composable
fun LoadingImageFromInternetCoil() {
    AsyncImage (model = ImageRequest.Builder(LocalContext.current)
        .data(URL)
        .build(),
        contentDescription = "Translated description of what the image contains"
    )
}
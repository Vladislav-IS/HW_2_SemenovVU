package com.semenov.hw2

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil3.compose.AsyncImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.collection.emptyLongSet
import androidx.constraintlayout.utils.widget.ImageFilterView
import coil3.request.ImageRequest
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy

const val URL = "https://github.com/Vladislav-IS/HW_2_SemenovVU/blob/main/qr.png?raw=true"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = Color.Cyan
            ) {
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                        .fillMaxSize()
                        .wrapContentWidth()
                        .wrapContentHeight()
                ) {
                    LoadImageFromInternet()
                }
            }
        }
    }
}

@Composable
fun LoadImageFromInternet() {
    AsyncImage (
        model = ImageRequest.Builder(LocalContext.current)
            .data(URL)
            .diskCachePolicy(
                CachePolicy.DISABLED)
            .memoryCachePolicy(
                CachePolicy.ENABLED)
            .networkCachePolicy(
                CachePolicy.ENABLED
            )
            .build(),
        contentDescription = "qr-code",
        alignment = Alignment.TopCenter,
        modifier = Modifier
            .size(300.dp),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.loading),
        error = painterResource(R.drawable.error),
    )
}

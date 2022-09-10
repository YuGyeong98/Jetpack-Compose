package com.example.jetpack_compose

import android.Manifest
import android.app.Application
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.PermissionRequest
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()
            var granted by remember { mutableStateOf(false) }
            val launcher =
                rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted -> // 권한 요청
                    granted = isGranted
                }

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                granted = true
            }

            if (granted) { // 권한 허용 상태
                viewModel.fetchPhotos()
                HomeScreen(photoUris = viewModel.photoUris.value)
            } else {
                PermissionRequestScreen {
                    launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }
}

class MainViewModel(application: Application) :
    AndroidViewModel(application) { // contentprovider 사용을 위해 androidviewmodel 사용
    private val _photoUris = mutableStateOf(emptyList<Uri>())
    val photoUris: State<List<Uri>> = _photoUris

    fun fetchPhotos() {
        val uris = mutableListOf<Uri>()

        getApplication<Application>().contentResolver.query( // getApplication()으로 context 얻기 가능
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC" // 찍은 순서 내림차순
        )?.use { cursor -> // 자동 close
            val idIndex =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID) // ID(데이터베이스에 실제로 저장됨)를 토대로 uri 생성 가능
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                uris.add(contentUri)
            }
        }
        _photoUris.value = uris
    }
}

@Composable
fun PermissionRequestScreen(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "권한이 허용되지 않았습니다")
        Button(onClick = onClick) {
            Text(text = "권한 요청")
        }
    }
}

@ExperimentalPagerApi
@Composable
fun HomeScreen(photoUris: List<Uri>) {
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            count = photoUris.size,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .fillMaxSize()
        ) { pageIndex ->
            Card(modifier = Modifier.graphicsLayer { // graphiclayer - 화면 애니메이션 효과
                val pageOffset = calculateCurrentOffsetForPage(pageIndex).absoluteValue
                lerp( // 사진의 위치에 따른 가로, 세로 크기 계산 공식
                    start = 0.85f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }

                alpha = lerp(start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f))
            }) {
                Image(
                    painter = rememberImagePainter(data = photoUris[pageIndex]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // 이미지가 꽉 차도록
                )
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float =
    (1 - fraction) * start + fraction * stop

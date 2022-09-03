package com.example.jetpack_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpack_compose.ui.theme.JetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isFavorite by rememberSaveable {
                mutableStateOf(false)
            }
            ImageCard(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(16.dp),
                isFavorite = isFavorite
            ) { favorite ->
                isFavorite = favorite
            }
        }
    }
}

//@Composable
//fun ImageCard() {
//    val isFavorite = remember { // 상태 기억
//        mutableStateOf(false) // isFavorite 상수는 MutableState 형식
//    }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth(0.5f)
//            .padding(16.dp),
//        shape = RoundedCornerShape(8.dp),
//        elevation = 5.dp
//    ) {
//        Box(modifier = Modifier.height(200.dp)) {
//            Image(
//                painter = painterResource(id = R.drawable.image),
//                contentDescription = "image",
//                contentScale = ContentScale.Crop
//            )
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) { // 이미지 위에 좋아요 기능
//                IconButton(onClick = { isFavorite.value = !isFavorite.value }) {
//                    Icon(
//                        imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
//                        contentDescription = "favorite",
//                        tint = Color.White
//                    )
//                }
//            }
//        }
//    }
//}

//@Composable
//fun ImageCard() {
//    var isFavorite by rememberSaveable { // by - value 생략 가능, rememberSaveable - 화면을 돌려도 설정 보존
//        mutableStateOf(false) // isFavorite 변수는 Boolean 형식
//    }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth(0.5f)
//            .padding(16.dp),
//        shape = RoundedCornerShape(8.dp),
//        elevation = 5.dp
//    ) {
//        Box(modifier = Modifier.height(200.dp)) {
//            Image(
//                painter = painterResource(id = R.drawable.image),
//                contentDescription = "image",
//                contentScale = ContentScale.Crop
//            )
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) { // 이미지 위에 좋아요 기능
//                IconButton(onClick = { isFavorite = !isFavorite }) {
//                    Icon(
//                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
//                        contentDescription = "favorite",
//                        tint = Color.White
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun ImageCard(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onTabFavorite: (Boolean) -> Unit
) { // 재사용 가능하도록 외부에서 들어오는 값으로 변경, 콜백 함수
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painterResource(id = R.drawable.image),
                contentDescription = "image",
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(onClick = { onTabFavorite(!isFavorite) }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "favorite",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

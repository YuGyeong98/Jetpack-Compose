package com.example.jetpack_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpack_compose.ui.theme.JetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val scrollState = rememberScrollState() // 스크롤 정보 기억

//            Column(
//                modifier = Modifier
//                    .background(color = Color.Green)
//                    .fillMaxWidth()
//                    .verticalScroll(scrollState)
//            ) {
//                for (i in 1..50) {
//                    Text("글씨 $i")
//                }
//            }

            LazyColumn(
                modifier = Modifier
                    .background(color = Color.Green)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp) // 아이템 간 간격 조절
            ) { // 스크롤이 자동으로 설정됨
                item {
                    Text(text = "Header")
                }
                items(50) { index ->
                    Text(text = "글씨 $index")
                }
                item {
                    Text(text = "Footer")
                }
            }
        }
    }
}

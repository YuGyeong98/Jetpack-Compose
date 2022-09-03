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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpack_compose.ui.theme.JetpackComposeTheme
import kotlinx.coroutines.launch

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            val textValue = remember {
//                mutableStateOf("")
//            }
//
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                TextField(value = textValue.value, onValueChange = {textValue.value = it}) // 텍스트 입력
//                Button(onClick = {}) {
//                    Text(text = "클릭")
//                }
//            }
//        }
//    }
//}

@ExperimentalComposeUiApi // 실험 기능
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val (text, setValue) = remember { // 구조 분해: 객체가 가지고 있는 여러 값을 분해해서 여러 변수에 한꺼번에 초기화
                mutableStateOf("")
            }

            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            val keyboardController = LocalSoftwareKeyboardController.current

            Scaffold(scaffoldState = scaffoldState) { // Material Component들을 사용할 수 있게 미리 디자인된 레이아웃, snackbar 사용하려면 필요
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(value = text, onValueChange = setValue)
                    Button(onClick = {
                        keyboardController?.hide() // null 안전한 호출 - ? 사용
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Hello $text") // showSnackbar 함수는 코루틴 스코프 사용 필요
                        }
                    }) {
                        Text(text = "클릭")
                    }
                }
            }
        }
    }
}

package com.example.jetpack_compose

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()
            HomeScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current

    val (inputUrl, setUrl) = rememberSaveable {
        mutableStateOf("https://www.google.com")
    }

    val scaffoldState = rememberScaffoldState() // 스낵바 띄우는 타이밍에 필요

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "나만의 웹 브라우저") },
            actions = {
                IconButton(onClick = { viewModel.undo() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { viewModel.redo() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "forward",
                        tint = Color.White
                    )
                }
            })
    }, scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = inputUrl,
                onValueChange = setUrl,
                label = { Text(text = "https://") }, // 힌트
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.url.value = inputUrl
                    focusManager.clearFocus() // 키보드 내려가는 효과
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            MyWebView(viewModel = viewModel, scaffoldState = scaffoldState)
        }
    }
}

@Composable
fun MyWebView(viewModel: MainViewModel, scaffoldState: ScaffoldState) {
    val webView = rememberWebView()

    LaunchedEffect(Unit) { // rememberCoroutineScope 와 비교, sharedflow와 launchedeffect 같이 사용하면 한번만 수행해야 되는 이벤트성 코드
        viewModel.undoSharedFlow.collectLatest { // 가장 최근 것만 관찰
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                scaffoldState.snackbarHostState.showSnackbar("더 이상 뒤로 갈 수 없음")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.redoSharedFlow.collectLatest {
            if (webView.canGoForward()) {
                webView.goForward()
            } else {
                scaffoldState.snackbarHostState.showSnackbar("더 이상 앞으로 갈 수 없음")
            }
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { webView }, // 화면에 표시해야 되는 뷰 객체 인스턴스 지정
        update = { webView ->
            webView.loadUrl(viewModel.url.value) // 화면 갱신
        }) // composition 다시 발생
}

@Composable
fun rememberWebView(): WebView {
    val context = LocalContext.current

    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true // 웹뷰 설정
            webViewClient = WebViewClient() // 웹뷰 설정
            loadUrl("https://google.com")
        }
    }
    return webView
}

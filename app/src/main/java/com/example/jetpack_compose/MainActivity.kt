package com.example.jetpack_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpack_compose.ui.theme.JetpackComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen(viewModel: MainViewModel = viewModel()) {
    val text1: MutableState<String> = remember {
        mutableStateOf("Hello World")
    }

    var text2: String by remember {
        mutableStateOf("Hello World")
    }

    val (text, setText) = remember {
        mutableStateOf("Hello World")
    }

    val text3: State<String> = viewModel.liveData.observeAsState("Hello World")

    Column() {
        Text(text = "Hello World")
        Button(onClick = {
            text1.value = "변경" // 1. text1
            print(text1.value)
            text2 = "변경" // 2. text2
            print(text2)
            setText("변경") // 3. (text, setText)
            viewModel.changeValue("변경") // 4. viewModel
        }) {
            Text(text = "클릭")
        }
        TextField(
            value = text,
            onValueChange = setText
        ) // recomposition이 일어남(글자 계속 수정하거나 입력하면 다시 화면에 그려짐)
    }
}

class MainViewModel : ViewModel() {
    private val _value: MutableState<String> =
        mutableStateOf("Hello World") // mutableStateOf - 읽기, 쓰기 가능
    val value: State<String> = _value // State - 읽기만 가능

    private val _liveData = MutableLiveData<String>()
    val liveData: LiveData<String> = _liveData

    fun changeValue(value: String) {
        _value.value = value
    }
}

package com.example.jetpack_compose

import android.Manifest
import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import android.webkit.PermissionRequest
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // 화면이 가로 모드로 고정되게 하기
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(viewModel)

        setContent {
            TiltScreen(x = viewModel.x.value, y = viewModel.y.value)
        }
    }
}

class MainViewModel(application: Application) : AndroidViewModel(application), SensorEventListener,
    LifecycleEventObserver {

    private val _x = mutableStateOf(0f)
    val x: State<Float> = _x

    private val _y = mutableStateOf(0f)
    val y: State<Float> = _y

    private val sensorManager by lazy { // lazy - sensorManager라는 상수가 처음 사용될 때, application에서 sensorManager를 얻어서 할당(늦은 초기화) -> 처음에 null인 상태를 줄 필요가 없음
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager // sensormanager로 캐스팅
    }

    // onresume, onpause에서 동작하도록
    fun registerSensor() {
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun unregisterSensor() {
        sensorManager.unregisterListener(this) // 화면이 안보일때는 센서 동작 안함
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            _x.value = event.values[0]
            _y.value = event.values[1]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_RESUME) {
            registerSensor()
        } else if (event == Lifecycle.Event.ON_PAUSE) {
            unregisterSensor()
        }
    }
}

@Composable
fun TiltScreen(x: Float, y: Float) {
    val yCoord = x * 20 // x, y 보정 - 더 많이 움직이는 것처럼 보이도록
    val xCoord = y * 20

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        drawCircle( // 바깥 원
            color = Color.Black,
            radius = 100f,
            center = Offset(centerX, centerY),
            style = Stroke()
        )
        drawCircle( // 녹색 원
            color = Color.Green,
            radius = 100f,
            center = Offset(xCoord + centerX, yCoord + centerY)
        )
        drawLine( // 가운데 십자가
            color = Color.Black,
            start = Offset(centerX - 20, centerY),
            end = Offset(centerX + 20, centerY)
        )
        drawLine( // 가운데 십자가
            color = Color.Black,
            start = Offset(centerX, centerY - 20),
            end = Offset(centerX, centerY + 20)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    TiltScreen(x = 30f, y = 20f)
}
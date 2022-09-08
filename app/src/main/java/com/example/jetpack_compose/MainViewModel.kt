package com.example.jetpack_compose

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val url = mutableStateOf("https://www.google.com")

    private val _undoSharedFlow = MutableSharedFlow<Boolean>() // viewmodel 내부에서 사용하기 위한 변경 가능한 flow
    // sharedflow - replayCache와 그 크기를 정의하여 새로운 구독자에게 반복 전달할 값의 개수를 설정, 구독자들을 Slot 이라는 형태로 관리하여 값이 전달 될 시 액티브한 모든 구독자에게 새로운 값이 전달
    val undoSharedFlow = _undoSharedFlow.asSharedFlow() // assharedflow - 외부로 노출하기 위해 변경 불가능한 Flow

    private val _redoSharedFlow = MutableSharedFlow<Boolean>()
    val redoSharedFlow = _undoSharedFlow.asSharedFlow()

    fun undo() {
        viewModelScope.launch {
            _undoSharedFlow.emit(true)
        }
    }

    fun redo() {
        viewModelScope.launch {
            _redoSharedFlow.emit(true)
        }
    }
}
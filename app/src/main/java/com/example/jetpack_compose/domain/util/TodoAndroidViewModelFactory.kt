package com.example.jetpack_compose.domain.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jetpack_compose.data.repository.TodoRepositoryImpl
import com.example.jetpack_compose.domain.repository.TodoRepository
import com.example.jetpack_compose.ui.main.MainViewModel

class TodoAndroidViewModelFactory(
    private val application: Application,
    private val repository: TodoRepository = TodoRepositoryImpl(application)
) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) { // mainviewmodel을 만드는 경우에 무엇을 할 것인지
            return MainViewModel(application, repository) as T
        }
        return super.create(modelClass)
    }
}
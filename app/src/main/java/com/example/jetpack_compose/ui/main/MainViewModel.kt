package com.example.jetpack_compose.ui.main

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack_compose.domain.model.Todo
import com.example.jetpack_compose.domain.repository.TodoRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application, private val todoRepository: TodoRepository) :
    AndroidViewModel(application) {
    private val _items = mutableStateOf(emptyList<Todo>())
    val items: State<List<Todo>> = _items

    private var recentlyDeleteTodo: Todo? = null

    init {
        viewModelScope.launch {
            todoRepository.observeTodos().collect { todos ->
                _items.value = todos
            }
        }
    }

    fun addTodo(text: String) {
        viewModelScope.launch {
            todoRepository.addTodo(Todo(title = text))
        }
    }

    fun toggle(uid: Int) {
        val todo = _items.value.find { todo -> todo.uid == uid }
        todo?.let {
            viewModelScope.launch {
                todoRepository.updateTodo(it.copy(isDone = !it.isDone).apply {
                    this.uid = it.uid
                })
            }
        }
    }

    fun delete(uid: Int) {
        val todo = _items.value.find { todo -> todo.uid == uid }
        todo?.let {
            viewModelScope.launch {
                todoRepository.deleteTodo(it)
                recentlyDeleteTodo = it
            }
        }
    }

    fun restoreTodo() {
        viewModelScope.launch {
            todoRepository.addTodo(
                recentlyDeleteTodo ?: return@launch
            ) // recentlyDeleteTodo가 null이라면 launch 취소
            recentlyDeleteTodo = null
        }
    }

}
package com.example.jetpack_compose.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.jetpack_compose.ui.main.components.TodoItem
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val scaffoldState = rememberScaffoldState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    var text by rememberSaveable { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text(text = "오늘 할 일") }) }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text(text = "할 일")},
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.addTodo(text)
                    text = ""
                    keyboardController?.hide()
                })
            )
            Divider()
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.items.value) { todoItem ->
                    Column {
                        TodoItem(
                            todo = todoItem,
                            onClick = { todo -> viewModel.toggle(todo.uid) },
                            onDeleteClick = { todo ->
                                viewModel.delete(todo.uid)
                                scope.launch {
                                    val result =
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "할 일 삭제됨",
                                            actionLabel = "취소"
                                        )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.restoreTodo()
                                    }
                                }
                            })
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.Black, thickness = 1.dp)
                    }
                }
            }
        }
    }
}
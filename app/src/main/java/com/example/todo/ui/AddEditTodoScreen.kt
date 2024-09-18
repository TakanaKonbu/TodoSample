package com.example.todo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.data.Todo

@Composable
fun AddEditTodoScreen(
    viewModel: TodoViewModel,
    todoId: Int? = null,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(todoId) {
        if (todoId != null) {
            val todo = viewModel.allTodos.value?.find { it.id == todoId }
            if (todo != null) {
                title = todo.title
                description = todo.description
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (title.isNotBlank()) {
                    if (todoId == null) {
                        viewModel.insertTodo(Todo(title = title, description = description))
                    } else {
                        viewModel.updateTodo(Todo(id = todoId, title = title, description = description))
                    }
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (todoId == null) "Add Todo" else "Update Todo")
        }
    }
}
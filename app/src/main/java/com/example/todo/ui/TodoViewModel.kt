package com.example.todo.ui

import androidx.lifecycle.*
import androidx.lifecycle.asLiveData
import com.example.todo.data.Todo
import com.example.todo.data.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    val allTodos: LiveData<List<Todo>> = repository.allTodos.asLiveData()

    fun insertTodo(todo: Todo) = viewModelScope.launch {
        repository.insertTodo(todo)
    }

    fun updateTodo(todo: Todo) = viewModelScope.launch {
        repository.updateTodo(todo)
    }

    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        repository.deleteTodo(todo)
    }
}

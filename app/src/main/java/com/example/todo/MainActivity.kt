package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo.data.TodoDatabase
import com.example.todo.data.TodoRepository
import com.example.todo.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp() {
    val navController = rememberNavController()
    val database = TodoDatabase.getDatabase(LocalContext.current)
    val repository = TodoRepository(database.todoDao())
    val viewModel: TodoViewModel = viewModel(
        factory = TodoViewModelFactory(repository)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TodoApp") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "todoList",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("todoList") {
                TodoListScreen(
                    viewModel = viewModel,
                    onAddTodo = { navController.navigate("addTodo") },
                    onEditTodo = { id -> navController.navigate("editTodo/$id") },
                    onTodoClick = { id -> navController.navigate("todoDetail/$id") }
                )
            }
            composable("addTodo") {
                AddEditTodoScreen(
                    viewModel = viewModel,
                    onSave = { navController.popBackStack() }
                )
            }
            composable(
                "editTodo/{todoId}",
                arguments = listOf(navArgument("todoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val todoId = backStackEntry.arguments?.getInt("todoId") ?: return@composable
                AddEditTodoScreen(
                    viewModel = viewModel,
                    todoId = todoId,
                    onSave = { navController.popBackStack() }
                )
            }
            composable(
                "todoDetail/{todoId}",
                arguments = listOf(navArgument("todoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val todoId = backStackEntry.arguments?.getInt("todoId") ?: return@composable
                TodoDetailScreen(
                    viewModel = viewModel,
                    todoId = todoId,
                    onEditClick = { id -> navController.navigate("editTodo/$id") },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

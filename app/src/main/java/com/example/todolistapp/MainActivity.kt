package com.example.todolistapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// TodoItem, TodoAction, and todoReducer code here

data class TodoItem(val id: Int, val task: String)

sealed class TodoAction {
    data class AddTask(val task: String) : TodoAction()
    data class RemoveTask(val id: Int) : TodoAction()
}

fun todoReducer(state: List<TodoItem>, action: TodoAction): List<TodoItem> {
    return when (action) {
        is TodoAction.AddTask -> state + TodoItem(id = state.size, task = action.task)
        is TodoAction.RemoveTask -> state.filter { it.id != action.id }
    }
}

class MainActivity : ComponentActivity() {
    private var todoList by mutableStateOf(listOf<TodoItem>())
    private var newTask by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("To-do List", style = MaterialTheme.typography.headlineLarge)

                BasicTextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    modifier = Modifier.padding(16.dp)
                )

                Button(onClick = {
                    if (newTask.isNotEmpty()) {
                        todoList = todoReducer(todoList, TodoAction.AddTask(newTask))
                        newTask = ""
                    } else {
                        Toast.makeText(this@MainActivity, "Please enter a task", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Save")
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(todoList) { todoItem ->
                        Text(
                            text = todoItem.task,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    todoList = todoReducer(todoList, TodoAction.RemoveTask(todoItem.id))
                                }
                        )
                    }
                }
            }
        }
    }
}

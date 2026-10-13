package com.example.taskmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.ui.screens.AddEditTaskScreen
import com.example.taskmanager.ui.screens.TaskListScreen
import com.example.taskmanager.ui.viewmodel.TaskViewModel

object Routes {
    const val TASK_LIST = "task_list"
    const val ADD_EDIT_TASK = "add_edit_task/{taskId}"
    
    fun addEditTask(taskId: Int = -1): String = "add_edit_task/$taskId"
}

@Composable
fun TaskNavigation(viewModel: TaskViewModel) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Routes.TASK_LIST
    ) {
        composable(Routes.TASK_LIST) {
            TaskListScreen(
                viewModel = viewModel,
                onAddTask = { navController.navigate(Routes.addEditTask(-1)) },
                onEditTask = { taskId -> navController.navigate(Routes.addEditTask(taskId)) }
            )
        }
        composable(Routes.ADD_EDIT_TASK) { backStackEntry ->
            val taskIdStr = backStackEntry.arguments?.getString("taskId")
            val taskId = taskIdStr?.toIntOrNull() ?: -1
            AddEditTaskScreen(
                viewModel = viewModel,
                taskId = taskId,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}

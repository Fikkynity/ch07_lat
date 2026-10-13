package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.data.seeder.DatabaseSeeder
import com.example.taskmanager.ui.navigation.TaskNavigation
import com.example.taskmanager.ui.theme.TaskManagerTheme
import com.example.taskmanager.ui.viewmodel.TaskViewModel
import com.example.taskmanager.ui.viewmodel.TaskViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    private val viewModel: TaskViewModel by viewModels {
        val app = application as TaskManagerApplication
        TaskViewModelFactory(app.taskRepository, app.categoryRepository)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Seed initial data
        val app = application as TaskManagerApplication
        val seeder = DatabaseSeeder(
            app.categoryRepository,
            app.taskRepository
        )
        lifecycleScope.launch {
            seeder.seedIfEmpty()
        }
        
        setContent {
            TaskManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskNavigation(viewModel = viewModel)
                }
            }
        }
    }
}

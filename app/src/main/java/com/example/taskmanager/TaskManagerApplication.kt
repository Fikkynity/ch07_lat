package com.example.taskmanager

import android.app.Application
import com.example.taskmanager.data.database.AppDatabase
import com.example.taskmanager.data.repository.CategoryRepository
import com.example.taskmanager.data.repository.TaskRepository

class TaskManagerApplication : Application() {
    
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
    
    val taskRepository: TaskRepository by lazy {
        TaskRepository(database.taskDao())
    }
    
    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(database.categoryDao())
    }
}

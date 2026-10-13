package com.example.taskmanager.data.seeder

import com.example.taskmanager.data.entity.Category
import com.example.taskmanager.data.entity.Task
import com.example.taskmanager.data.repository.CategoryRepository
import com.example.taskmanager.data.repository.TaskRepository
import kotlinx.coroutines.flow.first

class DatabaseSeeder(
    private val categoryRepository: CategoryRepository,
    private val taskRepository: TaskRepository
) {
    suspend fun seedIfEmpty() {
        val categories = categoryRepository.getAllCategories().first()
        
        if (categories.isEmpty()) {
            // Add default categories
            val workId = categoryRepository.insertCategory(
                Category(name = "Work", color = "#FF6B6B")
            )
            val personalId = categoryRepository.insertCategory(
                Category(name = "Personal", color = "#4ECDC4")
            )
            val shoppingId = categoryRepository.insertCategory(
                Category(name = "Shopping", color = "#95E1D3")
            )
            
            // Add sample tasks
            taskRepository.insertTask(
                Task(
                    title = "Complete project documentation",
                    description = "Write comprehensive docs for the new feature",
                    categoryId = workId.toInt(),
                    priority = 1
                )
            )
            
            taskRepository.insertTask(
                Task(
                    title = "Team meeting at 2 PM",
                    description = "Discuss Q4 roadmap",
                    categoryId = workId.toInt(),
                    priority = 0
                )
            )
            
            taskRepository.insertTask(
                Task(
                    title = "Buy groceries",
                    description = "Milk, eggs, bread, vegetables",
                    categoryId = shoppingId.toInt(),
                    priority = 0
                )
            )
            
            taskRepository.insertTask(
                Task(
                    title = "Exercise",
                    description = "30 minutes cardio",
                    categoryId = personalId.toInt(),
                    priority = 1
                )
            )
        }
    }
}

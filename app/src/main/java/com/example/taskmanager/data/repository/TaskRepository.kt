package com.example.taskmanager.data.repository

import com.example.taskmanager.data.dao.TaskDao
import com.example.taskmanager.data.entity.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    
    fun searchTasks(query: String): Flow<List<Task>> = taskDao.searchTasks(query)
    
    fun getTasksByCategory(categoryId: Int): Flow<List<Task>> = 
        taskDao.getTasksByCategory(categoryId)
    
    fun getActiveTasks(): Flow<List<Task>> = taskDao.getActiveTasks()
    
    suspend fun getTaskById(taskId: Int): Task? = taskDao.getTaskById(taskId)
    
    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)
    
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    
    suspend fun deleteCompletedTasks() = taskDao.deleteCompletedTasks()
}

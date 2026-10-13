package com.example.taskmanager.data.dao

import androidx.room.*
import com.example.taskmanager.data.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY created_at DESC")
    fun getAllTasks(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?
    
    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%'")
    fun searchTasks(query: String): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE category_id = :categoryId ORDER BY created_at DESC")
    fun getTasksByCategory(categoryId: Int): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE is_done = 0 ORDER BY priority DESC, created_at DESC")
    fun getActiveTasks(): Flow<List<Task>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long
    
    @Update
    suspend fun updateTask(task: Task)
    
    @Delete
    suspend fun deleteTask(task: Task)
    
    @Query("DELETE FROM tasks WHERE is_done = 1")
    suspend fun deleteCompletedTasks()
}

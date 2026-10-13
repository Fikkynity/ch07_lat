package com.example.taskmanager.data.repository

import com.example.taskmanager.data.dao.CategoryDao
import com.example.taskmanager.data.entity.Category
import com.example.taskmanager.data.relation.CategoryWithTasks
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {
    
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    
    fun getCategoriesWithTasks(): Flow<List<CategoryWithTasks>> = 
        categoryDao.getCategoriesWithTasks()
    
    suspend fun insertCategory(category: Category): Long = 
        categoryDao.insertCategory(category)
    
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
}

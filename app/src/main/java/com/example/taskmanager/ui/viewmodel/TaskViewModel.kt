package com.example.taskmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.entity.Task
import com.example.taskmanager.data.entity.Category
import com.example.taskmanager.data.repository.TaskRepository
import com.example.taskmanager.data.repository.CategoryRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false
)

@OptIn(FlowPreview::class)
class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _query = MutableStateFlow("")
    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    
    val query: StateFlow<String> = _query.asStateFlow()
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId.asStateFlow()
    
    val uiState: StateFlow<TaskUiState> = _query
        .debounce(300)
        .flatMapLatest { q ->
            if (q.isEmpty()) {
                _selectedCategoryId.value?.let { categoryId ->
                    taskRepository.getTasksByCategory(categoryId)
                } ?: taskRepository.getAllTasks()
            } else {
                taskRepository.searchTasks(q)
            }
        }
        .map { tasks ->
            TaskUiState(
                tasks = tasks,
                isLoading = false,
                isEmpty = tasks.isEmpty()
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            TaskUiState(isLoading = true)
        )
    
    val categories: StateFlow<List<Category>> = categoryRepository.getAllCategories()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )
    
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }
    
    fun selectCategory(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
        _query.value = ""
    }
    
    fun insertTask(task: Task) {
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
    }
    
    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }
    
    fun toggleDone(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isDone = !task.isDone))
        }
    }
    
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }
    
    fun exportActiveTasks(onExport: (String) -> Unit) {
        viewModelScope.launch {
            taskRepository.getActiveTasks().first().let { tasks ->
                val content = tasks.joinToString("\n") { task ->
                    val priorityStr = if (task.priority == 1) "[HIGH] " else ""
                    "$priorityStr${task.title}${if (task.description.isNotEmpty()) "\n  ${task.description}" else ""}"
                }
                onExport(content)
            }
        }
    }
}

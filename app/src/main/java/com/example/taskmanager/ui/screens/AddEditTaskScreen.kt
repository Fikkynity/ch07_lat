package com.example.taskmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.data.entity.Task
import com.example.taskmanager.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel,
    taskId: Int,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf(0) }
    var priority by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Load existing task if editing
    LaunchedEffect(taskId) {
        if (taskId != -1) {
            scope.launch {
                viewModel.uiState.value.tasks.find { it.id == taskId }?.let { task ->
                    title = task.title
                    description = task.description
                    selectedCategoryId = task.categoryId
                    priority = task.priority
                }
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }
    
    // Auto-select first category if none selected
    LaunchedEffect(categories) {
        if (taskId == -1 && categories.isNotEmpty() && selectedCategoryId == 0) {
            selectedCategoryId = categories.first().id
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == -1) "Add Task" else "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
                
                // Category dropdown
                if (categories.isNotEmpty()) {
                    var expanded by remember { mutableStateOf(false) }
                    
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "Select Category",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedCategoryId = category.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                // Priority selection
                Column {
                    Text(
                        text = "Priority",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = priority == 0,
                            onClick = { priority = 0 },
                            label = { Text("Normal") }
                        )
                        FilterChip(
                            selected = priority == 1,
                            onClick = { priority = 1 },
                            label = { Text("High") }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Save button
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            val task = Task(
                                id = if (taskId == -1) 0 else taskId,
                                title = title.trim(),
                                description = description.trim(),
                                categoryId = selectedCategoryId,
                                priority = priority
                            )
                            
                            if (taskId == -1) {
                                viewModel.insertTask(task)
                            } else {
                                viewModel.updateTask(task)
                            }
                            onSaved()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = title.isNotBlank()
                ) {
                    Text(if (taskId == -1) "Add Task" else "Save Changes")
                }
            }
        }
    }
}

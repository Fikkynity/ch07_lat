package com.example.taskmanager.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.taskmanager.data.entity.Category
import com.example.taskmanager.data.entity.Task

data class CategoryWithTasks(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val tasks: List<Task>
)

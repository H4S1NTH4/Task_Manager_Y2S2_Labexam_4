package com.example.taskmanagerr.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Time

/**
 * Task class represents a task entity in the database.
 */
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "completed")
    val completed: Int,

    @ColumnInfo(name = "deadline")
    val deadline: Date,

    @ColumnInfo(name = "time")
    val time: Time // New field for storing time

)

package com.example.taskmanagerr.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskmanagerr.dao.TaskDao
import com.example.taskmanagerr.entities.Task
import com.example.taskmanagerr.database.Converters

// TaskDatabase class is a Room database class that provides access to the database.
@Database(entities = [Task::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao // Abstract method to get the TaskDao

    // Companion object to get the database instance
    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null // Database instance

        fun getDatabase(context: Context): TaskDatabase { // Get the database instance
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
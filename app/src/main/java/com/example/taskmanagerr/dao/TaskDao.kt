package com.example.taskmanagerr.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.taskmanagerr.entities.Task

/**
 * TaskDao interface is a Data Access Object that provides an API for reading and writing tasks to the database.
 */
@Dao
interface TaskDao {
    @Insert(onConflict = REPLACE)
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM task_table WHERE id = :id")
    fun getTaskById(id: Int): LiveData<Task>

    @Query("SELECT * FROM task_table WHERE completed = 0 ORDER BY deadline ASC")
    fun getAllTasks(): LiveData<List<Task>>

    // pending count
    @Query("SELECT COUNT(*) FROM task_table WHERE completed = 0")
    fun getUncompletedTasksCount(): LiveData<Int>

    // today count
    @Query("SELECT COUNT(*) FROM task_table WHERE date(deadline / 1000 , 'unixepoch') = date('now', '-1 day', 'localtime') AND completed = 0")
    fun getTodayTasksCount(): LiveData<Int>

    // get today tasks
    @Query("SELECT * FROM task_table WHERE date(deadline / 1000 , 'unixepoch') = date('now', '-1 day', 'localtime') AND completed = 0")
    fun getTodayTasks(): LiveData<List<Task>>

    // overdue count
    @Query("SELECT COUNT(*) FROM task_table WHERE date(deadline / 1000 , 'unixepoch') < date('now', '-1 day', 'localtime') AND completed = 0")
    fun getOverdueTasksCount(): LiveData<Int>

    // get overdue tasks
    @Query("SELECT * FROM task_table WHERE date(deadline / 1000 , 'unixepoch') < date('now', '-1 day', 'localtime') AND completed = 0")
    fun getOverdueTasks(): LiveData<List<Task>>

    // completed count
    @Query("SELECT COUNT(*) FROM task_table WHERE completed = 1")
    fun getCompletedTasksCount(): LiveData<Int>

    // get completed tasks
    @Query("SELECT * FROM task_table WHERE completed = 1 ORDER BY deadline ASC")
    fun getCompletedTasks(): LiveData<List<Task>>
}
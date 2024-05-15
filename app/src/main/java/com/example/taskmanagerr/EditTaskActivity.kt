package com.example.taskmanagerr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagerr.R
import com.example.taskmanagerr.database.TaskDatabase
import com.example.taskmanagerr.entities.Task
import com.example.taskmanagerr.repositories.TaskRepository
import com.example.taskmanagerr.viewmodels.TaskViewModel
import com.example.taskmanagerr.viewmodels.TaskViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import java.sql.Time
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * EditTaskActivity class handles editing tasks.
 */
class EditTaskActivity : AppCompatActivity() {

    // UI components
    private lateinit var datePickerButton: Button
    private lateinit var backButton: Button
    private lateinit var saveTaskButton: Button
    private lateinit var titleInput: EditText
    private lateinit var bodyInput: EditText
    private lateinit var taskViewModel: TaskViewModel

    /**
     * Initializes the activity and sets up UI components and listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        // Extracting task ID from intent extras
        val taskId = intent.getIntExtra("TASK_ID", -1)

        // Initializing views
        datePickerButton = findViewById(R.id.editdatePickerButton)
        backButton = findViewById(R.id.editBackButton)
        saveTaskButton = findViewById(R.id.editSaveTaskButton)
        titleInput = findViewById(R.id.edittitleInput)
        bodyInput = findViewById(R.id.editbodyInput)

        // Create a date picker builder
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()

        // Build the date picker
        val datePicker = datePickerBuilder.build()

        // Setting click listener for back button
        backButton.setOnClickListener {
            backToMainActivity(this)
        }

        // Setting click listener for date picker button
        datePickerButton.setOnClickListener {
            // Show the date picker dialog when the button is clicked
            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        // Setting positive button click listener for date picker dialog
        datePicker.addOnPositiveButtonClickListener { selection ->
            val dateString = DateFormat.getDateInstance().format(Date(selection))
            datePickerButton.text = dateString
        }

        // Initializing ViewModel and observing task changes
        val taskDao = TaskDatabase.getDatabase(this).taskDao()
        val repository = TaskRepository(taskDao)
        val viewModelFactory = TaskViewModelFactory(repository)
        taskViewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]

        taskViewModel.getTaskById(taskId).observe(this) { task ->
            titleInput.setText(task.title)
            bodyInput.setText(task.description)

            // Convert task deadline date format
            val format = SimpleDateFormat("MMMM d, yyyy", Locale.US)
            datePickerButton.text = format.format(task.deadline)
        }

        // Setting click listener for save task button
        saveTaskButton.setOnClickListener {
            val title = titleInput.text.toString()
            val notes = bodyInput.text.toString()
            val dateString = datePickerButton.text.toString()

            // Parsing and formatting date string
            val format = SimpleDateFormat("dd MMMM yyyy", Locale.US)
            var utilDate: Date? = null
            if (dateString.isNotBlank()) {
                try {
                    println("Parsing date string: $dateString")
                    utilDate = format.parse(dateString)
                    println("Parsed date: $utilDate")
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            } else {
                println("dateString is null or empty")
            }
            val sqlDate = java.sql.Date(utilDate?.time ?: 0)

            // Creating task object with updated details
            val sqlTime = Time.valueOf("12:30:00") // Time value for 12:30 PM
            val task = Task(taskId, title, notes, 0, sqlDate, sqlTime)

            // Updating task in ViewModel
            taskViewModel.update(task)

            // Showing success message
            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show()

            // Navigating back to main activity
            backToMainActivity(this)
        }

    }

    /**
     * Navigates back to the main activity.
     * @param context The context from which the activity is navigated.
     */
    private fun backToMainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}

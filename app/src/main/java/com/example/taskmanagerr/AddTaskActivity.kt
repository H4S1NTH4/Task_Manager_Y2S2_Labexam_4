package com.example.taskmanagerr

import android.app.TimePickerDialog
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
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.sql.Time
import java.util.*

/**
 * AddTaskActivity class handles adding new tasks.
 */
class AddTaskActivity : AppCompatActivity() {

    // UI components
    private lateinit var datePickerButton: Button
    private lateinit var timePickerButton: Button
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
        setContentView(R.layout.activity_add_task)

        // Initializing views
        datePickerButton = findViewById(R.id.datePickerButton)
        timePickerButton = findViewById(R.id.timePickerButton)
        backButton = findViewById(R.id.backButton)
        saveTaskButton = findViewById(R.id.saveTaskButton)
        titleInput = findViewById(R.id.titleInput)
        bodyInput = findViewById(R.id.bodyInput)

        // Create a date picker builder
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()

        // Build the date picker
        val datePicker = datePickerBuilder.build()

        val now = Calendar.getInstance()

        timePickerButton.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(now.get(Calendar.HOUR_OF_DAY))
                .setMinute(now.get(Calendar.MINUTE))
                .setTitleText("Select Time")
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val selectedTime = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    timePicker.hour,
                    timePicker.minute
                )
                timePickerButton.text = selectedTime
            }

            timePicker.show(supportFragmentManager, "TimePickerDialog")
        }


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
            // Convert selected date to a human-readable date string and set it as the button text
            val dateString = DateFormat.getDateInstance().format(Date(selection))
            datePickerButton.text = dateString
        }

        // Initializing ViewModel
        val taskDao = TaskDatabase.getDatabase(this).taskDao()
        val repository = TaskRepository(taskDao)
        val viewModelFactory = TaskViewModelFactory(repository)
        taskViewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]

        // Setting click listener for save task button
        saveTaskButton.setOnClickListener {
            val title = titleInput.text.toString()
            val notes = bodyInput.text.toString()
            val dateString = datePickerButton.text.toString()
            val timeString = timePickerButton.text.toString()

            // Check if both date and time are selected
            if (dateString.isNotBlank() && timeString.isNotBlank()) {
                val format = SimpleDateFormat("dd MMMM yyyy", Locale.US)
                val utilDate = format.parse(dateString)

                val sqlDate = java.sql.Date(utilDate?.time ?: 0)

                val timeParts = timeString.split(":")
                val hour = timeParts[0].toInt()
                val minute = timeParts[1].toInt()
                val sqlTime = Time(hour, minute, 0)


                val task = Task(0, title, notes, 0, sqlDate, sqlTime)

                // Inserting task into ViewModel
                taskViewModel.insert(task)

                // Showing success message
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()

                // Navigating back to main activity
                backToMainActivity(this)
            } else {
                // Show error message if date or time is not selected
                Toast.makeText(this, "Please select both date and time", Toast.LENGTH_SHORT).show()
            }
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

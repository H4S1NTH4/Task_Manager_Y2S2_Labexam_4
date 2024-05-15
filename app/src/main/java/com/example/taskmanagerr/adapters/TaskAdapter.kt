package com.example.taskmanagerr.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerr.EditTaskActivity
import com.example.taskmanagerr.entities.Task
import com.example.taskmanagerr.R
import com.example.taskmanagerr.viewmodels.TaskViewModel

/**
 * TaskAdapter class is a RecyclerView adapter that provides data to the RecyclerView.
 */
class TaskAdapter(var tasks: List<Task>, private val taskViewModel: TaskViewModel) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // ViewHolder class that represents each item in the RecyclerView
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.notification_title_9)
        val description: TextView = itemView.findViewById(R.id.notification_body_9)
        val deadline: TextView = itemView.findViewById(R.id.notification_deadline_9)
        val notification: View = itemView.findViewById(R.id.notification_9)
        val time: TextView = itemView.findViewById(R.id.notification_time)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list, parent, false)
        return TaskViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.description.text = task.description
        holder.deadline.text = task.deadline.toString()
        holder.time.text = task.time.toString()

        holder.notification.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditTaskActivity::class.java)
            intent.putExtra("TASK_ID", task.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = tasks.size // Return the size of your dataset

    /**
     * Attaches the ItemTouchHelper to the RecyclerView.
     */
    fun attachToRecyclerView(recyclerView: RecyclerView, filter: String) {

        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Called when a user swipes left or right on a ViewHolder
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


                val position =
                    try {
                        viewHolder.adapterPosition
                    } catch (e: Exception) {
                        return
                    }

                if (direction == ItemTouchHelper.LEFT) {
                    try {
                        taskViewModel.delete(tasks[position]) // Delete the item from your list

                        // toasts a message to the user
                        val context = viewHolder.itemView.context
                        Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()

                    } catch (e: Exception) {

                        // toasts a message to the user
                        val context = viewHolder.itemView.context
                        Toast.makeText(context, "Completed tasks cannot be deleted", Toast.LENGTH_SHORT).show()

                    }
                } else if (direction == ItemTouchHelper.RIGHT) {

                    try {

                        val task = tasks[position].copy(completed = 1)
                        taskViewModel.update(task) // Set the task as completed

                        // toasts a message to the user
                        val context = viewHolder.itemView.context
                        Toast.makeText(context, "Task completed", Toast.LENGTH_SHORT).show()

                    } catch (e: Exception) {
                        // toasts a message to the user
                        val context = viewHolder.itemView.context
                        Toast.makeText(context, "Completed tasks cannot be completed", Toast.LENGTH_SHORT).show()
                    }


                }
            }
        }

        // Attach the ItemTouchHelper to the RecyclerView
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }
}
package com.taskscheduler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: MutableList<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // Нэг task карт дотор байгаа view-уудыг барина
    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView     = view.findViewById(R.id.tvTaskName)
        val tvDate: TextView     = view.findViewById(R.id.tvTaskDate)
        val tvTime: TextView     = view.findViewById(R.id.tvTaskTime)
        val tvCategory: TextView = view.findViewById(R.id.tvTaskCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.tvName.text     = task.name
        holder.tvDate.text     = task.date
        holder.tvTime.text     = "${task.startTime} - ${task.endTime}"
        holder.tvCategory.text = task.category

        // Ангиллын дагуу өнгө өөрчлөх
        val context = holder.itemView.context
        val (bgColor, textColor) = when (task.category) {
            "Work"  -> Pair(R.color.category_work,  R.color.primary)
            "Study" -> Pair(R.color.category_study, R.color.text_dark)
            else    -> Pair(R.color.category_life,  R.color.primary)  // Life
        }
        holder.tvCategory.setBackgroundColor(context.getColor(bgColor))
        holder.tvCategory.setTextColor(context.getColor(textColor))
    }

    override fun getItemCount() = tasks.size

    // Task жагсаалтыг шинэчлэх
    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}
package com.taskscheduler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var dbHelper: TaskDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        dbHelper = TaskDatabaseHelper(requireContext())

        // RecyclerView тохируулах
        taskAdapter = TaskAdapter(mutableListOf())
        view.findViewById<RecyclerView>(R.id.rvTasks).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }

        // Create Task товч
        view.findViewById<FloatingActionButton>(R.id.btnCreate).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CreateTaskFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    // Дэлгэц рүү буцаж ирэх бүрт task жагсаалтыг шинэчлэнэ
    override fun onResume() {
        super.onResume()
        taskAdapter.updateTasks(dbHelper.getAllTasks())
    }
}
package com.taskscheduler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Create new task товч дарахад CreateTaskFragment нээнэ
        val btnCreate = view.findViewById<FloatingActionButton>(R.id.btnCreate)
        btnCreate.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CreateTaskFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
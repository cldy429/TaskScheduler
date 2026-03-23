package com.taskscheduler

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import java.util.Calendar

class CreateTaskFragment : Fragment() {

    private var selectedCategory = "Life"
    private var selectedDate = ""
    private var selectedStartTime = ""
    private var selectedEndTime = ""

    private lateinit var dbHelper: TaskDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_task, container, false)

        dbHelper = TaskDatabaseHelper(requireContext())

        val etName     = view.findViewById<EditText>(R.id.etTaskName)
        val tvDate     = view.findViewById<TextView>(R.id.tvDate)
        val tvTime     = view.findViewById<TextView>(R.id.tvTime)
        val switchRem  = view.findViewById<SwitchCompat>(R.id.switchReminder)
        val btnLife    = view.findViewById<Button>(R.id.btnCategoryLife)
        val btnWork    = view.findViewById<Button>(R.id.btnCategoryWork)
        val btnStudy   = view.findViewById<Button>(R.id.btnCategoryStudy)

        // Буцах товч
        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Огноо сонгох
        view.findViewById<ImageButton>(R.id.btnSelectDate).setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                selectedDate = "%02d/%02d/%04d".format(d, m + 1, y)
                tvDate.text  = "%02d/%02d/%04d".format(d, m + 1, y)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Цаг сонгох
        view.findViewById<ImageButton>(R.id.btnSelectTime).setOnClickListener {
            val cal = Calendar.getInstance()
            // Эхлэх цаг
            TimePickerDialog(requireContext(), { _, startH, startM ->
                selectedStartTime = "%02d:%02d".format(startH, startM)
                // Дуусах цаг
                TimePickerDialog(requireContext(), { _, endH, endM ->
                    selectedEndTime = "%02d:%02d".format(endH, endM)
                    tvTime.text = "$selectedStartTime - $selectedEndTime"
                }, startH + 1, startM, true).show()
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        // Ангилал сонгох
        btnLife.setOnClickListener  { selectedCategory = "Life";  highlightCategory(btnLife, btnWork, btnStudy) }
        btnWork.setOnClickListener  { selectedCategory = "Work";  highlightCategory(btnWork, btnLife, btnStudy) }
        btnStudy.setOnClickListener { selectedCategory = "Study"; highlightCategory(btnStudy, btnLife, btnWork) }

        // CREATE TASK товч
        view.findViewById<Button>(R.id.btnCreateTask).setOnClickListener {
            val name = etName.text.toString().trim()

            // Шалгалт
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Даалгаврын нэр оруулна уу!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedDate.isEmpty()) {
                Toast.makeText(requireContext(), "Огноо сонгоно уу!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Task объект үүсгэж database-д хадгална
            val task = Task(
                name      = name,
                date      = selectedDate,
                startTime = selectedStartTime,
                endTime   = selectedEndTime,
                category  = selectedCategory,
                remind    = switchRem.isChecked
            )
            dbHelper.insertTask(task)

            Toast.makeText(requireContext(), "Даалгавар үүслээ ✓", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }

        return view
    }

    // Сонгосон ангилалын товчийг тодруулах
    private fun highlightCategory(active: Button, vararg others: Button) {
        active.backgroundTintList = requireContext().getColorStateList(R.color.primary)
        active.setTextColor(requireContext().getColor(android.R.color.white))
        others.forEach {
            it.setBackgroundColor(android.graphics.Color.parseColor("#F0F0F0"))
            it.setTextColor(android.graphics.Color.parseColor("#2D2D2D"))
        }
    }
}
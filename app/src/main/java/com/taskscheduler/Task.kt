package com.taskscheduler

data class Task(
    val id: Long = 0,        // Давтагдашгүй дугаар (SQLite автоматаар өгнө)
    val name: String,        // Даалгаврын нэр
    val date: String,        // Огноо "25/03/2026"
    val startTime: String,   // Эхлэх "09:00"
    val endTime: String,     // Дуусах "11:00"
    val category: String,    // Ангилал "Life", "Work", "Study"
    val remind: Boolean      // Сануулах уу? true/false
)
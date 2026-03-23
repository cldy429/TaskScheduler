package com.taskscheduler

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "task_scheduler.db", null, 1) {

    // Хүснэгтийн нэр болон баганын нэрнүүд
    companion object {
        const val TABLE = "tasks"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_DATE = "date"
        const val COL_START = "start_time"
        const val COL_END = "end_time"
        const val COL_CATEGORY = "category"
        const val COL_REMIND = "remind"
    }

    // Хүснэгт үүсгэх (app анх ажиллахад нэг удаа дуудагдана)
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT NOT NULL,
                $COL_DATE TEXT,
                $COL_START TEXT,
                $COL_END TEXT,
                $COL_CATEGORY TEXT,
                $COL_REMIND INTEGER DEFAULT 0
            )
        """.trimIndent())
    }

    // Database шинэчлэгдэхэд дуудагдана
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    // Даалгавар хадгалах
    fun insertTask(task: Task): Long {
        val values = ContentValues().apply {
            put(COL_NAME, task.name)
            put(COL_DATE, task.date)
            put(COL_START, task.startTime)
            put(COL_END, task.endTime)
            put(COL_CATEGORY, task.category)
            put(COL_REMIND, if (task.remind) 1 else 0)
        }
        return writableDatabase.insert(TABLE, null, values)
    }

    // Бүх даалгавруудыг авах
    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val cursor = readableDatabase.query(
            TABLE, null, null, null, null, null, "$COL_ID DESC"
        )
        while (cursor.moveToNext()) {
            tasks.add(Task(
                id        = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                name      = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                date      = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)) ?: "",
                startTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_START)) ?: "",
                endTime   = cursor.getString(cursor.getColumnIndexOrThrow(COL_END)) ?: "",
                category  = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)) ?: "",
                remind    = cursor.getInt(cursor.getColumnIndexOrThrow(COL_REMIND)) == 1
            ))
        }
        cursor.close()

        // Огноогоор эрэмбэлэх (хамгийн ойрын огноо дээр)
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        return tasks.sortedBy { task ->
            try { sdf.parse(task.date) } catch (e: Exception) { null }
        }
    }
}
package com.example.todoapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    val name: String,
    val important: Boolean = false,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val categoryNumber: Int = 1,
    val execTime: Long = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val createDateFormatted: String get() = DateFormat.getDateTimeInstance().format(created)

    val execTimeDateFormatted: String get() = SimpleDateFormat("dd/MM HH:mm", Locale.ENGLISH).format(execTime)
}
package com.example.timglog

import android.text.SpannableString
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks"
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val name:String,

    val category: String,

    val startTime: Long,
    val endTime: Long

){



}
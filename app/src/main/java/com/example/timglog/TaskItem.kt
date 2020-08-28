package com.example.timglog

import org.joda.time.DateTime

sealed class TaskItem{
    data class Day(val day : DateTime) : TaskItem()
    data class TaskEntry(val task: Task) : TaskItem()

}
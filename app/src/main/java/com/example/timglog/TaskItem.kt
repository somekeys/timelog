package com.example.timglog

import java.time.LocalDate
import java.time.LocalDateTime


sealed class TaskItem{
    data class Day(val day : LocalDateTime) : TaskItem()
    data class TaskEntry(val task: Task) : TaskItem()

}
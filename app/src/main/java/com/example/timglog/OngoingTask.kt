package com.example.timglog

import androidx.room.ColumnInfo

data class OngoingTask constructor(
    val name:String = "Foo",

    val category: String = "Foo",

    val seconds: Int = 0,

    val state : TaskState = TaskState.STOPPED

){

    enum class TaskState{
        STARTED,PAUSED,STOPPED
    }


}
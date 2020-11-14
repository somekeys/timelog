package com.example.timglog

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class TaskDao{

    @Insert
    abstract fun insert(task:Task) :Long

    @Query("SELECT * from tasks ORDER BY endTime DESC")
    abstract fun getAll(): LiveData<List<Task>>
}
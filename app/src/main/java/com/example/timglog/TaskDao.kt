package com.example.timglog

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class TaskDao{

    @Insert
    abstract fun insert(task:Task) :Long

    @Delete
    abstract fun delete(task: Task)

    @Query("SELECT * from tasks ORDER BY endTime DESC")
    abstract fun getAll(): LiveData<List<Task>>


    @Query("SELECT DISTINCT category from tasks ORDER BY endTime DESC")
    abstract fun getCats():LiveData<List<String>>

    @Query("SELECT * from tasks Where name = :name  ORDER BY endTime DESC LIMIT 1")
    abstract  fun getLastByName(name:String): Task?

}
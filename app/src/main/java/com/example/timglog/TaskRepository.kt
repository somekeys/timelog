package com.example.timglog

import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allTasks: LiveData<List<Task>> = taskDao.getAll()
    val cats:LiveData<List<String>> = taskDao.getCats()


     fun insert(task: Task) {
        taskDao.insert(task)
    }

    fun getLastByName(name:String): Task? {
        return taskDao.getLastByName(name)
    }


}
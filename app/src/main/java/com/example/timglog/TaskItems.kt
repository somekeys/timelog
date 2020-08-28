package com.example.timglog

import org.joda.time.DateTime

object TaskItems {

    fun convertList(tasks : List<Task>): MutableList<TaskItem> {
        var  taskItemList = mutableListOf<TaskItem>()
        var  daytotasks  : HashMap<DateTime,MutableList<TaskItem>> = HashMap<DateTime,MutableList<TaskItem>>()
        tasks.forEach {
            if(daytotasks.containsKey(DateTime(it.endTime).withMillisOfDay(0))){
                daytotasks[DateTime(it.endTime).withMillisOfDay(0)] = mutableListOf<TaskItem>()
            }
            daytotasks[DateTime(it.endTime).withMillisOfDay(0)]!!.add(TaskItem.TaskEntry(it))}
        val  daytotasks_sorted = daytotasks.toSortedMap()
        daytotasks_sorted.forEach { (k,v) ->
            taskItemList.add(TaskItem.Day(k))
            taskItemList.addAll(v)
        }

        return taskItemList.asReversed()


    }
}
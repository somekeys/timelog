package com.example.timglog

import java.time.*

object TaskItems {

    fun convertList(tasks : List<Task>): MutableList<TaskItem> {
        var  taskItemList = mutableListOf<TaskItem>()
        var  daytotasks  : HashMap<Long,MutableList<TaskItem>> = HashMap<Long,MutableList<TaskItem>>()
        tasks.forEach {
            val dayTime = it.endTime - it.endTime%(60*60*24)
            if(!daytotasks.containsKey(dayTime) ){
                daytotasks[dayTime] = mutableListOf<TaskItem>()
            }
            daytotasks[dayTime]!!.add(TaskItem.TaskEntry(it))}
        val  daytotasks_sorted = daytotasks.toSortedMap()
        daytotasks_sorted.forEach { (k,v) ->
            taskItemList.addAll(v)
            taskItemList.add( TaskItem.Day(LocalDateTime.ofEpochSecond(k,0,OffsetDateTime.now().offset)))
        }

        return taskItemList.asReversed()


    }
}
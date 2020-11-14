package com.example.timglog

import java.time.*

object TaskItems {

    fun convertList(tasks : List<Task>): MutableList<TaskItem> {
        var  taskItemList = mutableListOf<TaskItem>()
        var  daytotasks  : HashMap<Long,MutableList<TaskItem>> = HashMap<Long,MutableList<TaskItem>>()

        var currentDay  : LocalDateTime = LocalDateTime.ofEpochSecond(0,0,OffsetDateTime.now().offset)
        tasks.forEach {
            val dayTime = LocalDateTime.ofEpochSecond(it.endTime,0,OffsetDateTime.now().offset)
            if(currentDay.year != dayTime.year || currentDay.dayOfYear != dayTime.dayOfYear  ){
                currentDay = dayTime
                taskItemList.add( TaskItem.Day(currentDay))
            }
            taskItemList.add(TaskItem.TaskEntry(it))
//            if(!daytotasks.containsKey(dayTime) ){
//                daytotasks[dayTime] = mutableListOf<TaskItem>()
//            }
//            daytotasks[dayTime]!!.add(TaskItem.TaskEntry(it))
        }
//        val  daytotasks_sorted = daytotasks.toSortedMap()
//        daytotasks_sorted.forEach { (k,v) ->
//            taskItemList.addAll(v)
//            taskItemList.add( TaskItem.Day(LocalDateTime.ofEpochSecond(k,0,OffsetDateTime.now().offset)))
//        }

        return taskItemList


    }
}
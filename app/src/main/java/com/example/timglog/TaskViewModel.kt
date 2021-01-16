package com.example.timglog

import android.app.Application
import android.graphics.Typeface
import android.os.Handler
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.abs
import kotlin.math.log10

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository


    private var running : Boolean = false
    val handler : Handler = Handler()
    val prefs =getDefaultSharedPreferences(getApplication())


    val alltasks: LiveData<List<Task>>
    val categories: LiveData<List<String>>

    val title : MutableLiveData<String> = MutableLiveData()
    val category : MutableLiveData<String> = MutableLiveData()
    var startTime:MutableLiveData<Long> = MutableLiveData()
    val duration_text : MutableLiveData<SpannableString>   = MutableLiveData()
    var nameTaskMap = hashMapOf<String, Task>()
    enum class TaskState{
        STARTED,PAUSED,STOPPED
    }
    var stopwatchState : TaskState = TaskState.STOPPED

    private var seconds : Int = 0

    // paramotor
    private var onTomato :Boolean = false
    private var numTomato = 0

    init {
        val tasksDao = TimeLogDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(tasksDao)
        alltasks = repository.allTasks
        categories = repository.cats
        updateStopwatch()
    }

    fun isRunning(): Boolean {
        return running
    }
    fun updateStopwatch(){
        startTime.value = prefs.getLong("sw_start_time" , System.currentTimeMillis()/1000)
        title.value = prefs.getString("sw_title","Foo")
        category.value = prefs.getString("sw_category","Foo")
        stopwatchState = TaskState.values()[prefs.getInt("sw_state",TaskState.STOPPED.ordinal)]

        if(stopwatchState == TaskState.STARTED){
            running = true
            runTimer()
        }
    }


    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(task)
        }
    }

    fun getLastByName(name: String): Task? {
        return nameTaskMap[name]
    }

    fun startTask(task_title: String, task_category: String){
        if(running){stopTask()}
        val editor = prefs.edit()
//        title.value = task_title
//        category.value = task_category
//        startTime.value = System.currentTimeMillis()/1000
//        seconds = 0
        running = true
        editor.putLong("sw_start_time", System.currentTimeMillis()/1000)
            .putString("sw_title", task_title)
            .putString("sw_category", task_category)
            .putInt("sw_state",TaskState.STARTED.ordinal)
        editor.apply()
        updateStopwatch()
    }



    private fun runTimer(){
        handler.post(object : Runnable {


            override fun run() {

                duration_text.value = secondsToSpaning((System.currentTimeMillis()/1000 - startTime.value!!).toInt())

                if (running) {
                    handler.postDelayed(this, 1000)

                }


            }
        })
    }

    fun stopTask( ){
        running = false
        handler.removeCallbacksAndMessages(null)
        insert(
            Task(
                0, title.value!!, category.value!!, startTime.value!!,
                System.currentTimeMillis() / 1000
            )
        )

    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(task: Task)  {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(task)
        }
    }
    companion object {

        fun secondsToSpaning(seconds: Int): SpannableString {
            val hours: Int = seconds / 3600
            val minutes: Int = (seconds % 3600) / 60
            val secs = seconds % 60
            var time: String = String.format(
                Locale.getDefault(), "%dh%dm%ss", hours,
                minutes, secs
            )

            fun Int.length() = when (this) {
                0 -> 1
                else -> log10(abs(toDouble())).toInt() + 1
            }

            var styleTime: SpannableString = SpannableString(time)

            val relaSize = 1.5f
            if (hours != 0) {
                styleTime.setSpan(StyleSpan(Typeface.NORMAL), 0, hours.length(), 0)
                styleTime.setSpan(RelativeSizeSpan(relaSize), 0, hours.length(), 0)

                styleTime.setSpan(
                    StyleSpan(Typeface.NORMAL),
                    hours.length() + 1,
                    hours.length() + 1 + minutes.length(),
                    0
                )
                styleTime.setSpan(
                    RelativeSizeSpan(relaSize),
                    hours.length() + 1,
                    hours.length() + 1 + minutes.length(),
                    0
                )

                styleTime.setSpan(
                    StyleSpan(Typeface.NORMAL),
                    hours.length() + 1 + minutes.length() + 1,
                    hours.length() + 1 + minutes.length() + 1 + secs.length(),
                    0
                )
                styleTime.setSpan(
                    RelativeSizeSpan(relaSize),
                    hours.length() + 1 + minutes.length() + 1,
                    hours.length() + 1 + minutes.length() + 1 + secs.length(),
                    0
                )


            } else {
                if (minutes != 0) {
                    time = String.format(
                        Locale.getDefault(), "%dm%ss",
                        minutes, secs
                    )
                    styleTime = SpannableString(time)
                    styleTime.setSpan(StyleSpan(Typeface.NORMAL), 0, minutes.length(), 0)
                    styleTime.setSpan(RelativeSizeSpan(relaSize), 0, minutes.length(), 0)

                    styleTime.setSpan(
                        StyleSpan(Typeface.NORMAL),
                        minutes.length() + 1,
                        minutes.length() + 1 + secs.length(),
                        0
                    )
                    styleTime.setSpan(
                        RelativeSizeSpan(relaSize),
                        minutes.length() + 1,
                        minutes.length() + 1 + secs.length(),
                        0
                    )

                } else {
                    time = String.format(
                        Locale.getDefault(), "%ss",
                        secs
                    )
                    styleTime = SpannableString(time)
                    styleTime.setSpan(StyleSpan(Typeface.NORMAL), 0, secs.length(), 0)
                    styleTime.setSpan(RelativeSizeSpan(relaSize), 0, secs.length(), 0)


                }
            }

            return styleTime

        }
    }
}
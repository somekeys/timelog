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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.*
import kotlin.math.abs
import kotlin.math.log10

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository


    private var running : Boolean = false
    val handler : Handler = Handler()

    val alltasks: LiveData<List<Task>>

    val title : MutableLiveData<String> = MutableLiveData()
    val category : MutableLiveData<String> = MutableLiveData()
    var startTime:MutableLiveData<Long> = MutableLiveData()
    val duration_text : MutableLiveData<SpannableString>   = MutableLiveData()
    private var seconds : Int = 0


    init {
        val tasksDao = TimeLogDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(tasksDao)
        alltasks = repository.allTasks
        duration_text.value = SpannableString("")
        startTime.value = DateTime.now().millis
        title.value = "Foo"
        category.value = "foo"


    }

    fun startTask( task_title : String, task_category : String ){
        if(running){stopTask()}
        title.value = task_title
        category.value = task_category
        startTime.value = DateTime.now().millis
        seconds = 0
        running = true
        runTimer()

    }



    private fun runTimer(){
        handler.post( object :Runnable {


            override fun  run()
            {

                duration_text.value = secondsToSpaning(seconds)

                if (running) {
                    seconds++;
                    handler.postDelayed(this, 1000)

                }


            }
        })
    }

    fun stopTask( ){
        running = false
        handler.removeCallbacksAndMessages(null)
        insert(Task(0,title.value!!,category.value!!,startTime.value!!,
            DateTime.now().millis ))

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
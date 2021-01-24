package com.example.timglog

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager


class TomatoClock(application: Application) {

    enum class State {
        STARTED, PAUSED, STOPPED
    }

    // long break every x pomos
    private var breakIntervalLarge = 3

    // time length in minutes
    private var pomoLength = 25
    private var breakLengthSmall = 5
    private var breakLengthLarge = 25

    // num of passed timer
    private var timerCounter = 0

    // num of obtained tomatoes
    private val numPomos get() = timerCounter / 2

    // if the next timer is a break
    private val isBreakNext get() = (timerCounter % 2) == 1

    // if the next timer is for a large break
    private val isLargeBreak get() = isBreakNext && ((numPomos % breakIntervalLarge) == 0)

    // ongoing orin minutes
     val curTimerLength :Int
        get() {
            if (isBreakNext) {
                //check if it's large break
                if (isLargeBreak) {
                    return breakLengthLarge
                } else {
                    return breakLengthSmall
                }
            }else{
               return  pomoLength
            }
        }

    private var laststartTime: Long = 0
    private lateinit var mTimer: CountDownTimer
    val pomodoroTime: MutableLiveData<Int> = MutableLiveData(0)

    var tomatoState = MutableLiveData(State.STOPPED)
    private var running = false

    val prefs = PreferenceManager.getDefaultSharedPreferences(application)

    init {
        restore()
    }

    fun runTimer() {


        tomatoState.value = State.STARTED
        laststartTime = System.currentTimeMillis()/1000

        // create timer
        mTimer = object : CountDownTimer((curTimerLength * 60 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                pomodoroTime.value = (millisUntilFinished/1000).toInt()
            }

            override fun onFinish() {
                finishTomato()
            }
        }.start()


    }

    fun finishTomato() {
        timerCounter += 1
        prefs.edit().putInt("sw_state",
                State.STOPPED.ordinal).apply()
        tomatoState.value = State.STOPPED

    }

    fun restore() {
        laststartTime = prefs.getLong("tm_start_time", System.currentTimeMillis() / 1000)
        tomatoState.value = State.values()[prefs.getInt("sw_state",
                State.STOPPED.ordinal)]

    }
}

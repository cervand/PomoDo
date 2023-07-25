package com.example.myapplication

import android.os.CountDownTimer
import android.widget.TextView

/*
Enum class used to define the states of the PomodoroTimer. Used for informatic purposes for anyone
who wishes to implement and get data from a PomodoroTimer object.
 */
enum class PomodoroState{
    PASSIVE,
    WORKING,
    SHORT_BREAK,
    LONG_BREAK,
    PAUSED
}

/*
workLength, shortBreakLength, and longBreakLength are all in minutes and converted to milliseconds in this class.
workRounds is a regular thing.
 */
class PomodoroTimer(var workLength: Long = 25*60000,
                    var shortBreakLength: Long = 5*60000,
                    var longBreakLength: Long = 15*60000,
                    var totalWorkRounds: Int = 4,
                    var textViewObject: TextView)
{
    var totalMillisecondsLeft: Long = 0
    var currentState: PomodoroState = PomodoroState.PASSIVE


    private var tempTimer: CountDownTimer? = null
    private var workRoundsCompleted = 0
    private lateinit var beforePauseState: PomodoroState

    private var workTimer = object: CountDownTimer(workLength, 1000){
        override fun onTick(millisUntilFinished: Long) {
            totalMillisecondsLeft = millisUntilFinished
            updateTextView(millisUntilFinished)
        }

        override fun onFinish(){
            workRoundsCompleted++
            startPomodoroStage()
        }
    }

    private var shortBreakTimer = object: CountDownTimer(shortBreakLength, 1000){
        override fun onTick(millisUntilFinished: Long) {
            totalMillisecondsLeft = millisUntilFinished
            updateTextView(millisUntilFinished)
        }

        override fun onFinish(){
            startPomodoroStage()
        }
    }

    private var longBreakTimer = object: CountDownTimer(longBreakLength, 1000){
        override fun onTick(millisUntilFinished: Long) {
            totalMillisecondsLeft = millisUntilFinished
            updateTextView(millisUntilFinished)
        }

        override fun onFinish(){
            startPomodoroStage()
        }
    }

    fun resetAll(){
        pauseWork()
        pauseLongBreak()
        pauseShortBreak()
        tempTimer?.cancel()
        updateTextView(workLength)
        currentState = PomodoroState.PASSIVE
        workRoundsCompleted = 0
    }

    fun startPomodoroStage(){
        when(currentState){
            PomodoroState.PASSIVE-> startWorking()
            PomodoroState.WORKING -> {
                if(workRoundsCompleted<totalWorkRounds){startShortBreak()}
                else {startLongBreak()}
            }
            PomodoroState.SHORT_BREAK -> startWorking()
            PomodoroState.LONG_BREAK -> resetAll()
            PomodoroState.PAUSED -> {
                currentState = beforePauseState
                startNewTimer(totalMillisecondsLeft)
            }
            else -> {}
        }
    }




    fun pausePomodoro(){
        pauseWork()
        pauseLongBreak()
        pauseShortBreak()
        tempTimer?.cancel()
        beforePauseState = currentState
        currentState = PomodoroState.PAUSED
    }

    private fun startNewTimer(lengthOfTimer: Long){
        tempTimer = object: CountDownTimer(lengthOfTimer, 1000){
            override fun onTick(millisUntilFinished: Long) {
                totalMillisecondsLeft = millisUntilFinished
                updateTextView(millisUntilFinished)
            }

            override fun onFinish(){
                startPomodoroStage()
            }
        }.start()
    }

    private fun startWorking(){
        workTimer.start()
        currentState = PomodoroState.WORKING
    }

    private fun startShortBreak(){
        shortBreakTimer.start()
        currentState = PomodoroState.SHORT_BREAK
    }

    private fun startLongBreak(){
        longBreakTimer.start()
        currentState = PomodoroState.LONG_BREAK
    }

    private fun pauseWork(){
        workTimer.cancel()
    }

    private fun pauseShortBreak(){
        shortBreakTimer.cancel()
    }

    private fun pauseLongBreak(){
        longBreakTimer.cancel()
    }

    private fun updateTextView(millisecondsLeft: Long){
        val minutesLeft: Long = millisecondsLeft/60000
        val secondsLeftFromMinute: Long = (millisecondsLeft%60000)/1000
        val newText = "${minutesLeft}:${secondsLeftFromMinute}"
        textViewObject.setText(newText)
    }//end updateTextView

}//end class
package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var timeDisplay: TextView = findViewById(R.id.theTextView)
        var pomodoroTimer = PomodoroTimer(textViewObject = timeDisplay)
        val startPauseButton: Button = findViewById<Button>(R.id.startPauseCont)
        val resetButton: Button = findViewById<Button>(R.id.resetButton)

        startPauseButton.setOnClickListener{
            if(pomodoroTimer.currentState == PomodoroState.PASSIVE || pomodoroTimer.currentState == PomodoroState.PAUSED){
                pomodoroTimer.startPomodoroStage()
            }
            else{
                pomodoroTimer.pausePomodoro()
            }
        }

        resetButton.setOnClickListener { pomodoroTimer.resetAll() }
    }
}
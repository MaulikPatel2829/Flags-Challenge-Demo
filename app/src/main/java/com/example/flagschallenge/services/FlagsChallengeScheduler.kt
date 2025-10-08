package com.example.flagschallenge.services

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

object FlagsChallengeScheduler {

    private const val NEXT_QUESTION_REQUEST_CODE = 325
    private const val START_NEXT_QUESTION = "START_NEXT_QUESTION"

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNextQuestion(
        context: Context,
        questionIndex: Int,
        totalTimeInSec: Long
    ) {
        val totalTimeInMillis = totalTimeInSec * 1000L
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = START_NEXT_QUESTION
            putExtra("NEXT_QUESTION_INDEX", questionIndex)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, NEXT_QUESTION_REQUEST_CODE,
            intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, totalTimeInMillis,
            pendingIntent
        )

    }

    fun cancelScheduleEvent(context: Context, questionIndex: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = START_NEXT_QUESTION
            putExtra("NEXT_QUESTION_INDEX", questionIndex)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NEXT_QUESTION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
        Log.i("cancelScheduleEvent", "Next question alarm cancelled.")
    }

}
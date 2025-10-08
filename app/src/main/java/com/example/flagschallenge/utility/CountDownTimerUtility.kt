package com.example.flagschallenge.utility

import android.os.CountDownTimer
import android.util.Log

class CountDownTimerUtility(
    var onTicking: (millisUntilFinished: Long) -> Unit,
    var onFinished: () -> Unit
) {

    lateinit var countDownTimer: CountDownTimer

    fun startCountDownTimer(totalTimeInSec: Long, intervalTimeInSec: Long){
        //1000 MS = 1 Sec
        val totalTimeInMillis = totalTimeInSec * 1000L
        val intervalInMillis = intervalTimeInSec * 1000L
        Log.d("CntDwnTmerUtlty", "totalTimeInMillis=$totalTimeInMillis - intervalInMillis=$intervalInMillis")
        cancelCountDownTimer()
        countDownTimer = object : CountDownTimer(totalTimeInMillis,intervalInMillis){
            override fun onFinish() {
                onFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                onTicking(secondsRemaining)
            }

        }.start()
    }

    private fun cancelCountDownTimer(){
        if (::countDownTimer.isInitialized){
            countDownTimer.cancel()
        }
    }

}
package com.example.flagschallenge.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.flagschallenge.R
import com.example.flagschallenge.ui.FlagChallengeActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == "START_FIRST_QUESTION"){
            val nextQuestionIndex = intent.getIntExtra("NEXT_QUESTION_INDEX",1)
            Log.i("AlarmReceiver", "10-second interval finished. Time to load Question $nextQuestionIndex.")
            val intent = Intent(context, FlagChallengeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = "START_FIRST_QUESTION"
                putExtra("NEXT_QUESTION_INDEX",nextQuestionIndex)
            }
            context?.startActivity(intent)
//            setNotification(context!!, nextQuestionIndex)
            setNotification(context!!, 1, intent)
        }
        if (intent?.action == "START_NEXT_QUESTION"){
            val nextQuestionIndex = intent.getIntExtra("NEXT_QUESTION_INDEX",1)
            Log.i("AlarmReceiver", "10-second interval finished. Time to load Question $nextQuestionIndex.")
            val intent = Intent(context, FlagChallengeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = "START_NEXT_QUESTION"
                putExtra("load_question",true)
                putExtra("NEXT_QUESTION_INDEX",nextQuestionIndex)
            }
            context?.startActivity(intent)
//            setNotification(context!!, nextQuestionIndex)
            setNotification(context!!, 1, intent)
        }
    }

    private fun setNotification(context: Context, questionIndex: Int, intent: Intent) {
        val channelId = "challenge_channel_id"



        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,"Alarm Channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(context,System.currentTimeMillis().toInt(),
            intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Flag Challenge")
            .setContentText("Interval over! Question $questionIndex is ready.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)){
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            } else notify(10,notificationBuilder.build())
        }
    }
}
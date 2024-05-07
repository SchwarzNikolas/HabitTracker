package com.example.habittracker.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.habittracker.MainActivity
import com.example.habittracker.R

class NotificationService(
    private val context: Context
){
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE        // This?
        )
<<<<<<< HEAD
        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_cruelty_free_24)
            .setContentTitle("RootReflect")
            .setContentText("Don't forget to log your mood!")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(1, notification)
=======
        val notification = NotificationCompat
>>>>>>> 7428f21ae9cb3f3b47ae6be252ee580538f0700e
    }

    companion object {
        const val COUNTER_CHANNEL_ID = "counter_channel"
    }
}
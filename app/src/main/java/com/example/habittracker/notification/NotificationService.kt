package com.example.habittracker.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.habittracker.MainActivity

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
        // val notification = NotificationCompat
    }

    companion object {
        const val COUNTER_CHANNEL_ID = "counter_channel"
    }
}
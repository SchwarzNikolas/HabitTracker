package com.habittracker.rootreflect.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.habittracker.rootreflect.MainActivity
import com.habittracker.rootreflect.R

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
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_cruelty_free_24)
            .setContentTitle("RootReflect")
            .setContentText("Don't forget to log your mood!")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        const val COUNTER_CHANNEL_ID = "counter_channel"
    }
}
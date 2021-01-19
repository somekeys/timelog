package com.example.timglog

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

internal class StopWatchNotification {

     fun createNotificationChannel(notificationManager: NotificationManagerCompat) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Stopwatch"
            val descriptionText = "For task stopwatch"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel("StopwatchNoti", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun build(context: Context, baseTime: Long, isrunning: Boolean,notiTitle :String): Notification {
// Create an explicit intent for an Activity in your app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notiView = RemoteViews(context.getPackageName(), R.layout.stopwatch_noti)
        // title and stopwatch
        notiView.setChronometer(R.id.stopwatch_noti_timer, baseTime, null, isrunning)
        notiView.setTextViewText(R.id.stopwatch_noti_title,notiTitle)

        val builder = NotificationCompat.Builder(context, "StopwatchNoti")
            .setLocalOnly(true)
            .setOngoing(isrunning)
            .setSmallIcon(R.drawable.ic_baseline_adjust_24)
            .setCustomContentView(notiView)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    return builder.build()
    }

}
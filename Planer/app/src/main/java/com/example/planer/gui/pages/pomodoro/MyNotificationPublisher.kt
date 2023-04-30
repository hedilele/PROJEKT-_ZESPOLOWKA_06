package com.example.planer.gui.pages.pomodoro

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.planer.R

class MyNotificationPublisher: BroadcastReceiver()
{
    companion object {
        const val CHANNEL_ID = "my_notification_channel"
        const val CHANNEL_NAME = "My Notification Channel"
        const val CHANNEL_DESCRIPTION = "This is a channel for my test notification"
        const val NOTIFICATION_ID = 1
        const val MY_NOTIFICATION_ACTION = "my_notification_action"
    }

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, p1: Intent?) {
        if (p1?.action == MY_NOTIFICATION_ACTION) {
            createNotificationChannel(context)
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_whatshot)
                .setContentTitle("Notification Title")
                .setContentText("Notification Text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID,builder.build())
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
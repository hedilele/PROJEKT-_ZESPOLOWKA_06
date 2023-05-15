package com.example.planer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

//Klasa odbiorcy - odbieranie i wysweitlanie powiadomien
class NotificationReceiver: BroadcastReceiver()
{
    override fun onReceive(p0: Context, p1: Intent)
    {
        val calendarId = p1.getLongExtra("calendarId", -1)
        val name = p1.getStringExtra("name")

        val notificationManager = ContextCompat.getSystemService(
            p0,
            NotificationManager::class.java
        ) as NotificationManager

        // Tworzenie kanału powiadomień (jeśli korzystasz z Androida 8.0 i nowszych)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(
                "default_channel_id",
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    /*
    val openAppIntent = Intent(p0, MainActivity::class.java)
    openAppIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    val openIntent = PendingIntent.getActivity(
        p0,
        taskId,
        openAppIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
     */

    val notification = buildNotification(p0, name ?: "")
    notificationManager.notify(calendarId.toInt(), notification)
}

private fun buildNotification(context: Context, name: String): Notification {
    return NotificationCompat.Builder(context, "default_channel_id")
        .setContentTitle(name) // Ustawianie nazwy zadania jako tytułu powiadomienia
        .setSmallIcon(R.drawable.ic_stat_whatshot)
        .setSound(Uri.parse("android.resource://" + context.packageName + "/raw/notification_sound.mp3"))
        .setVibrate(longArrayOf(0, 1000, 500, 1000))
        .setAutoCancel(true)
        .build()
    //.setContentIntent(openIntent)
    }
}
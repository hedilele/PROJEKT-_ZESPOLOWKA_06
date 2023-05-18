package com.example.planer.norification

import android.app.Service
import android.content.Intent
import android.os.IBinder
/**
 * Klasa odpowiedzialna za wysylanie powiadomien o wydarzeniach z kalendarza po zamknieciu aplikacji
 */
class NotificationService: Service()
{
    private lateinit var notificationHelper: NotificationHelper

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val calendarId = intent?.getLongExtra("calendarId", -1)
        val name = intent?.getStringExtra("name")
        val startDate = intent?.getStringExtra("startDate")
        val reminder = intent?.getIntExtra("reminder", 0)

        if (calendarId != null && calendarId.toInt() != -1 && name != null && startDate != null && reminder != null) {
            notificationHelper.scheduleNotification(startDate, calendarId,reminder, name)
        }
        return START_STICKY
    }
}
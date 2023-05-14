package com.example.planer

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Klasa odpowiedzialna za wysylanie powiadomien o taskach po zamknieciu aplikacji
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
        val taskId = intent?.getIntExtra("taskId", -1)
        val taskName = intent?.getStringExtra("taskName")
        val deadline = intent?.getStringExtra("deadline")

        if (taskId != null && taskId != -1 && taskName != null && deadline != null) {
            notificationHelper.scheduleNotification(deadline, taskId, taskName)
        }
        return START_STICKY
    }
}
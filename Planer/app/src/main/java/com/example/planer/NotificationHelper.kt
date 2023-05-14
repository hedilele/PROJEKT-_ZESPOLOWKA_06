package com.example.planer

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.*
/**
 *Klasa odpowiedzialna za wysylanie informacji i tresci do receivera
 *Znajduje sie w niej cala logika dotyczaca powiadomien
 */
class NotificationHelper(private val context: Context)
{
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleNotification(deadline: String, taskId: Int, taskName: String)
    {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val deadlineDate = dateFormat.parse(deadline)

        if(deadlineDate != null)
        {
            val currentTime = System.currentTimeMillis()
            val oneHourBeforeDeadline = deadlineDate.time - (60*60*1000) // odejmuje godzinie w milisekundach

            if(deadlineDate.time > currentTime && deadlineDate.time > oneHourBeforeDeadline)
            {
                val timeDiff = deadlineDate.time - currentTime
                val oneHourInMillis = 60 * 60 * 1000

                if(timeDiff >= oneHourInMillis)
                {
                    val notificationIntent = Intent(context, NotificationReceiver::class.java)
                    notificationIntent.putExtra("taskId", taskId)
                    notificationIntent.putExtra("taskName", taskName)

                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        taskId,
                        notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE)
                    alarmManager.set(AlarmManager.RTC_WAKEUP, deadlineDate.time - oneHourInMillis, pendingIntent)
                }
            }
        }
    }
}
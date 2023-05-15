package com.example.planer.norification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*
/**
 *Klasa odpowiedzialna za wysylanie informacji i tresci do receivera
 *Znajduje sie w niej cala logika dotyczaca powiadomien
 */
class NotificationHelper(private val context: Context)
{
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    fun scheduleNotification(startDate: String, calendarId: Long,reminder: Int, name: String)
    {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val deadlineDate = dateFormat.parse(startDate)

        if(deadlineDate != null)
        {
            var currentTime = System.currentTimeMillis()
            val oneHourBeforeDeadline = deadlineDate.time - (60*60*1000) // odejmuje godzinie w milisekundach

            if(deadlineDate.time > currentTime) // && deadlineDate.time > oneHourBeforeDeadline
            {
                val timeDiff = deadlineDate.time - currentTime
                //val oneHourInMillis = 60 * 60 * 1000
                var timeInMillis = 0
                if(reminder == 0)
                {
                    return
                }
                else if(reminder == 1)
                {
                    timeInMillis = 15 * 60 * 1000
                }
                else if(reminder == 2)
                {
                    timeInMillis = 60 * 60 * 1000
                }
                else if(reminder == 3)
                {
                    timeInMillis = 24 * 60 * 60 * 1000
                }

                if(timeDiff >= timeInMillis)
                {
                    val notificationIntent = Intent(context, NotificationReceiver::class.java)
                    notificationIntent.putExtra("calendarId", calendarId)
                    notificationIntent.putExtra("name", name)

                    //Do wysylania powiadomien
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        calendarId.toInt(),
                        notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE)
                    alarmManager.set(AlarmManager.RTC_WAKEUP, deadlineDate.time - timeInMillis, pendingIntent)

                }
            }
        }
    }
}
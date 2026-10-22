package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R
import com.example.model.Holiday

class HolidayNotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "bd_gov_holiday_channel"
        const val CHANNEL_NAME = "Government Holiday Reminders"
        const val CHANNEL_DESC = "Notifications for upcoming Bangladesh government holidays and observances."
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showHolidayNotification(holiday: Holiday, isTomorrow: Boolean, isBengali: Boolean) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val title = if (isBengali) {
            if (isTomorrow) "আগামীকাল সরকারি ছুটি: ${holiday.nameBn}" else "আজ সরকারি ছুটি: ${holiday.nameBn}"
        } else {
            if (isTomorrow) "Tomorrow is a Holiday: ${holiday.nameEn}" else "Today is a Holiday: ${holiday.nameEn}"
        }

        val text = if (isBengali) holiday.descriptionBn else holiday.descriptionEn

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(context).notify(holiday.id.hashCode(), builder.build())
        } catch (e: SecurityException) {
            // Handled if permission was revoked
        }
    }
}

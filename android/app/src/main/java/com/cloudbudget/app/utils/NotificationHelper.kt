package com.cloudbudget.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cloudbudget.app.R

object NotificationHelper {

    private const val CHANNEL_ID = "cloudbudget_alerts"
    private const val CHANNEL_NAME = "Budget Alerts"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when cloud spending exceeds budget"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun sendOverBudgetAlert(context: Context, provider: String, actual: Double, allocated: Double) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_warning)
            .setContentTitle("Over Budget Alert!")
            .setContentText("${provider.uppercase()} spending ($${String.format("%.2f", actual)}) exceeded budget ($${String.format("%.2f", allocated)})")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify(provider.hashCode(), notification)
    }
}

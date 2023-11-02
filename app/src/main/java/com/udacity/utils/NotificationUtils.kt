package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.R
import com.udacity.detail.DetailActivity

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0


/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, status: String, fileName: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches this activity
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
        .putExtra(DetailActivity.EXTRA_DETAIL_STATUS, status)
        .putExtra(DetailActivity.EXTRA_DETAIL_FILENAME, fileName)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.load_notification_channel_id)
    )
        .setContentTitle(applicationContext
            .getString(
                R.string.notification_title))
        .setContentText(messageBody)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(
            0,
            applicationContext.getString(R.string.notification_action_details),
            contentPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Call notify
    notify(NOTIFICATION_ID, builder.build())
}




/**
 * Cancels all notifications.
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}

package com.example.singupactivity.ui.main.Notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Activity.SplashActivity
import com.example.singupactivity.ui.main.Objects.Notification.ArgumentsNotification

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(p0: Context?, p1: Intent?) {

        val intent = Intent(p0, SplashActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(p0, 0, intent, 0)


        val builder = p0?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(p0.getString(R.string.notification))
                .setContentText("Мероприятие \"${ArgumentsNotification.nameEvent}\" состоится в ${ArgumentsNotification.timeEvent}")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(BitmapFactory.decodeResource(p0.resources,
                    R.drawable.ic_baseline_notifications_active_24))
                .setContentIntent(pendingIntent)

        }

        var notificationManagerCompat = NotificationManagerCompat.from(p0!!)
        notificationManagerCompat.notify(NOTIFICATION_ID, builder!!.build())
    }

}
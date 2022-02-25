package com.example.singupactivity.ui.main.Notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Activity.SplashActivity
import android.graphics.Bitmap

import android.graphics.drawable.VectorDrawable

import android.os.Build

import android.annotation.TargetApi
import android.graphics.Canvas


class AlarmReceiver(
) : BroadcastReceiver() {

    companion object {
        var NOTIFICATION_ID = 101
        const val CHANNEL_ID = "CHANNEL_ID"
        const val DATE_EVENT_EXTRA = "DATE_EVENT_EXTRA"
        const val TIME_EVENT_EXTRA = "TIME_EVENT_EXTRA"
        const val NAME_EVENT_EXTRA = "NAME_EVENT_EXTRA"
        const val IS_EVENT = "IS_EVENT"
        const val GROUP_KEY = "GROUP_KEY"


    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context, intent: Intent) {

        val intentNotification = Intent(context, SplashActivity::class.java)
        intentNotification.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intentNotification, 0)

        val text = (if(intent.getBooleanExtra(IS_EVENT, false)) "Мероприятие" else "Событие") +
                " \"${intent.getStringExtra(NAME_EVENT_EXTRA)}\" " +
                "состоится ${intent.getStringExtra(DATE_EVENT_EXTRA)} в" +
                " ${intent.getStringExtra(TIME_EVENT_EXTRA)}"

        val title = if (intent.getBooleanExtra(IS_EVENT, false))

            context.getString(R.string.notificationEvent)
        else
            context.getString(R.string.notificationDS)

        val builder = context.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.logo_img
                    )
                )
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(title))
                .setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setDefaults(
                    Notification.DEFAULT_SOUND or
                            Notification.DEFAULT_VIBRATE
                )
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID++, builder.build())
    }

}
package com.fourteen.digital.pushnotificatinscloudmessagingfb

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    // Assign channel ID
    val channel_id = "notification_channel"
    val channel_name = "com.fourteen.digital.pushnotificatinscloudmessagingfb"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
                Log.d("notification",remoteMessage.notification!!.title!! )
            showNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!)
        }
    }

    // Method to get the custom Design for the display of
    // notification.
    @SuppressLint("RemoteViewLayout")
    private fun getCustomDesign(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews("com.fourteen.digital.pushnotificatinscloudmessagingfb",
            R.layout.notifications)
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(R.id.icon, R.drawable.ic_bell)
        return remoteViews
    }

    // Method to display the notifications
    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(title: String, message: String) {
        // Pass the intent to switch to the MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        // Pass the intent to PendingIntent to start the
        // next Activity
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channel_id)
            .setSmallIcon(R.drawable.ic_bell)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000,
                1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.

        builder = builder.setContent(getCustomDesign(title, message))

        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channel_id, channel_name,
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }

}
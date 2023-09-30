package by.bashlikovvv.music_player.android

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.nio.channels.Channel

class PlayerForegroundService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationLayout = RemoteViews(packageName, R.layout.player_notification_large)

        val customNotification = NotificationCompat.Builder(this, Notification.EXTRA_NOTIFICATION_ID)
            .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayout)
            .build()

        notificationManager.notify(666, customNotification)

        return super.onStartCommand(intent, flags, startId)
    }
}
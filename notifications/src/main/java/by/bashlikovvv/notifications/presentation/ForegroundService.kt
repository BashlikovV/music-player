package by.bashlikovvv.notifications.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import by.bashlikovvv.notifications.R
import by.bashlikovvv.notifications.domain.Actions.ACTION_NEXT
import by.bashlikovvv.notifications.domain.Actions.ACTION_PAUSE
import by.bashlikovvv.notifications.domain.Actions.ACTION_PLAY
import by.bashlikovvv.notifications.domain.Actions.ACTION_PREPARE
import by.bashlikovvv.notifications.domain.Actions.ACTION_PREVIOUS
import by.bashlikovvv.notifications.domain.Actions.ACTION_SET_TIME
import by.bashlikovvv.notifications.domain.Actions.KEY_TIME

class ForegroundService : Service() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_PREPARE -> {
//                val path = intent.extras?.getString(KEY_PATH) ?: ""
//                val fileName = intent.extras?.getString(KEY_FILE_NAME) ?: ""
            }
            ACTION_PLAY -> {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notificationChannel  = NotificationChannel(
                    "CHANNEL_ID",
                    "Foreground Service",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(notificationChannel)

                val notificationLayout = RemoteViews(packageName, R.layout.player_notification_small)
                val notificationLayoutExpanded = RemoteViews(packageName, R.layout.player_notification_large)

                val pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                val pendingPlayIntent = PendingIntent.getBroadcast(
                    this, 0, Intent(ACTION_PLAY), pendingIntentFlags
                )
                val pendingNextIntent = PendingIntent.getBroadcast(
                    this, 0, Intent(ACTION_NEXT), pendingIntentFlags
                )
                val pendingPreviousIntent = PendingIntent.getBroadcast(
                    this, 0, Intent(ACTION_PREVIOUS), pendingIntentFlags
                )
                notificationLayoutExpanded.apply {
                    setOnClickPendingIntent(R.id.startStopBtn, pendingPlayIntent)
                    setOnClickPendingIntent(R.id.skipNextBtn, pendingNextIntent)
                    setOnClickPendingIntent(R.id.skipPreviousBtn, pendingPreviousIntent)
                }

                val customNotification = NotificationCompat.Builder(this, "CHANNEL_ID")
                    .setSmallIcon(R.drawable.play_arrow)
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(notificationLayout)
                    .setCustomBigContentView(notificationLayoutExpanded)
                    .build()

                notificationManager.notify(666, customNotification)

                timer {
                    sendBroadcast(Intent(ACTION_SET_TIME).apply {
                        putExtra(KEY_TIME, it)
                    })
                }.start()
            }
            ACTION_PAUSE -> {

            }
        }
        return START_NOT_STICKY
    }

    class LocalBinder : Binder()

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder()
    }


    private fun timer(callback: (Long) -> Unit) = object : CountDownTimer(12000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            callback(millisUntilFinished)
        }

        override fun onFinish() { return }
    }

}
package by.bashlikovvv.notifications.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.RemoteViews
import android.widget.Toast
import by.bashlikovvv.notifications.R

class ActionsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            ForegroundService.ACTION_PLAY -> {
                Toast.makeText(context, "play action", Toast.LENGTH_LONG).show()
            }
            ForegroundService.ACTION_NEXT -> {
                Toast.makeText(context, "next action", Toast.LENGTH_LONG).show()
            }
            ForegroundService.ACTION_PREVIOUS -> {
                Toast.makeText(context, "previous action", Toast.LENGTH_LONG).show()
            }
            ForegroundService.ACTION_SET_TIME -> {
                val time = intent.extras?.getLong(ForegroundService.KEY_TIME)
                RemoteViews(context?.packageName, R.layout.player_notification_large).apply {
                }
            }
        }
    }

    val filter = IntentFilter().apply {
        addAction(ForegroundService.ACTION_PREVIOUS)
        addAction(ForegroundService.ACTION_PLAY)
        addAction(ForegroundService.ACTION_NEXT)
        addAction(ForegroundService.ACTION_SET_TIME)
    }
}
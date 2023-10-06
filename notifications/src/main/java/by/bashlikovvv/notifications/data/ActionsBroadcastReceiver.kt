package by.bashlikovvv.notifications.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.RemoteViews
import android.widget.Toast
import by.bashlikovvv.notifications.R
import by.bashlikovvv.notifications.domain.Actions.ACTION_NEXT
import by.bashlikovvv.notifications.domain.Actions.ACTION_PLAY
import by.bashlikovvv.notifications.domain.Actions.ACTION_PREVIOUS
import by.bashlikovvv.notifications.domain.Actions.ACTION_SET_TIME
import by.bashlikovvv.notifications.domain.Actions.KEY_TIME

class ActionsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            ACTION_PLAY -> {
                Toast.makeText(context, "play action", Toast.LENGTH_LONG).show()
            }
            ACTION_NEXT -> {
                Toast.makeText(context, "next action", Toast.LENGTH_LONG).show()
            }
            ACTION_PREVIOUS -> {
                Toast.makeText(context, "previous action", Toast.LENGTH_LONG).show()
            }
            ACTION_SET_TIME -> {
                val time = intent.extras?.getLong(KEY_TIME)
                RemoteViews(context?.packageName, R.layout.player_notification_large).apply {
                }
            }
        }
    }

    val filter = IntentFilter().apply {
        addAction(ACTION_PREVIOUS)
        addAction(ACTION_PLAY)
        addAction(ACTION_NEXT)
        addAction(ACTION_SET_TIME)
    }
}
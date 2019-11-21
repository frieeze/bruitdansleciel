package contre.facon.bruitdansleciel.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import contre.facon.bruitdansleciel.service.Player

class NotifBroadcastReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val local: Intent = Intent()
        local.action = intent?.action
        context?.sendBroadcast(local)
    }
}
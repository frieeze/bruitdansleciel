package contre.facon.bruitdansleciel.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import contre.facon.bruitdansleciel.`interface`.PlayerListener

class ServiceReceiver(val mListener: PlayerListener) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            mListener.onNotifyClick(intent.action.toString())
        }
    }
}
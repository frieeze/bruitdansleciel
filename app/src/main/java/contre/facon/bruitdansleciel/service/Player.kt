package contre.facon.bruitdansleciel.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.progur.droidmelody.SongFinder
import contre.facon.bruitdansleciel.R
import contre.facon.bruitdansleciel.reciever.NotifBroadcastReciever
import contre.facon.bruitdansleciel.utils.Constants

class Player(val context: Context, val notificationManager: NotificationManager) {
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    fun playSong(song: SongFinder.Song) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, song.uri);
        mediaPlayer.prepare()
        mediaPlayer.start()
        playerNotification(song)
    }

    fun pauseSong() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()

        } else {
            mediaPlayer.start()
        }
    }

    fun playerNotification(song: SongFinder.Song) {
        if (notificationManager == null) return

        val pauseIntent = Intent(context, NotifBroadcastReciever::class.java).apply {
            action = Constants.Intents.CF_PLAYER_PAUSE
        }

        val pausePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, pauseIntent, 0)

        val builder =
            NotificationCompat.Builder(context, "bruitdansleciel_notify_channe")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(song.title)
                .setContentText(song.artist + " - " + song.album)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.icon_search, "", pausePendingIntent)


        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }


}
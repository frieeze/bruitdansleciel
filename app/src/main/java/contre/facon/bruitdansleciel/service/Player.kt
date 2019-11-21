package contre.facon.bruitdansleciel.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.progur.droidmelody.SongFinder
import contre.facon.bruitdansleciel.R
import contre.facon.bruitdansleciel.reciever.NotifBroadcastReciever
import contre.facon.bruitdansleciel.utils.Constants
import kotlin.random.Random

class Player(
    val context: Context,
    val notificationManager: NotificationManager,
    var songList: List<SongFinder.Song>,
    var random: Boolean
) {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private lateinit var currentSong: SongFinder.Song


    fun playSong(song: SongFinder.Song) {
        currentSong = song
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, song.uri)
        mediaPlayer.setOnCompletionListener {
            playNextSong()
        }
        mediaPlayer.prepare()
        mediaPlayer.start()
        playerNotification(song)
    }

    fun playPauseSong() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }

    fun setLoop() {
        mediaPlayer.isLooping = !mediaPlayer.isLooping
    }

    fun setRandom() {
        random = !random
    }

    fun playNextSong() {
        if (mediaPlayer.isLooping || songList.size <= 1) {
            return
        }
        if (random) {
            val index = Random.nextInt(0, songList.size - 1)
            playSong(songList[index])
            return
        }
        val index = songList.indexOf(currentSong)
        if (index >= songList.size - 1) {
            mediaPlayer.reset()
            return
        }
        playSong(songList[index + 1])


    }

    fun playPreviousSong() {
        val index = songList.indexOf(currentSong)
        if (index == 0) {
            playSong(currentSong)
        }
        playSong(songList[index - 1])

    }

    fun updateSongList(newList: List<SongFinder.Song>) {
        songList = newList
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

    fun getPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun getLoop(): Boolean {
        return mediaPlayer.isLooping
    }

}
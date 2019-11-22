package contre.facon.bruitdansleciel.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.progur.droidmelody.SongFinder
import contre.facon.bruitdansleciel.R
import contre.facon.bruitdansleciel.`interface`.PlayerListener
import contre.facon.bruitdansleciel.reciever.NotifBroadcastReciever
import contre.facon.bruitdansleciel.reciever.ServiceReceiver
import contre.facon.bruitdansleciel.utils.Constants
import org.jetbrains.anko.AnkoLogger
import kotlin.random.Random

class Player(
    val context: Context,
    val mListener: PlayerListener,
    val notificationManager: NotificationManager,
    var songList: List<SongFinder.Song>
) {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var currentSong: SongFinder.Song? = null
    private var loop: Boolean = false
    private var random: Boolean = false


    fun playSong(song: SongFinder.Song) {
        if (song == null) return
        currentSong = song
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, song.uri)
        mediaPlayer.setOnCompletionListener {
            playNextSong()
        }
        mediaPlayer.prepare()
        mediaPlayer.isLooping = loop
        mediaPlayer.start()
        mListener.onPlaySong(song)
        playerNotification(song)
    }

    fun playPauseSong(): Boolean {
        if (currentSong == null) return false
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
        playerNotification(currentSong!!)
        return true
    }

    fun setLoop() {
        if (currentSong != null) {
            mediaPlayer.isLooping = !mediaPlayer.isLooping
        }
        loop = !loop
    }

    fun setRandom() {
        random = !random
    }

    fun playNextSong() {
        if (currentSong == null) return
        if (mediaPlayer.isLooping) {
            playSong(currentSong!!)
            return
        }
        val index = songList.indexOf(currentSong!!)
        if (songList.size <= 1 || index >= songList.size - 1) {
            mediaPlayer.reset()
            currentSong = null
            mListener.onPlayPauseButtonChange()
            mListener.onPlayerStop()
            removeNotification()
            return
        }
        if (random) {
            val index = Random.nextInt(0, songList.size - 1)
            playSong(songList[index])
            return
        }
        playSong(songList[index + 1])


    }

    fun playPreviousSong() {
        if (currentSong == null) return
        val index = songList.indexOf(currentSong!!)
        if (index == 0) {
            playSong(currentSong!!)
            return
        }
        playSong(songList[index - 1])

    }

    fun updateSongList(newList: List<SongFinder.Song>) {
        songList = newList
    }


    fun playerNotification(song: SongFinder.Song) {
        if (notificationManager == null) return
        val title = if (mediaPlayer.isPlaying) "PAUSE" else "PLAY"
        val icon = if (mediaPlayer.isPlaying) R.drawable.pause_button else R.drawable.play_button


        val pauseIntent = Intent(context, NotifBroadcastReciever::class.java).apply {
            action = Constants.Intents.CF_PLAYER_PAUSE
        }

        val previousIntent = Intent(context, NotifBroadcastReciever::class.java).apply {
            action = Constants.Intents.CF_PLAYER_PREVIOUS
        }
        val nextIntent = Intent(context, NotifBroadcastReciever::class.java).apply {
            action = Constants.Intents.CF_PLAYER_NEXT
        }

        val pausePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, pauseIntent, 0)
        val previousPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, previousIntent, 0)
        val nextPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, nextIntent, 0)


        val builder =
            NotificationCompat.Builder(context, "bruitdansleciel_notify_channe")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(song.title)
                .setContentText(song.artist + " - " + song.album)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.previous_button, "PREVIOUS", previousPendingIntent)
                .addAction(icon, title, pausePendingIntent)
                .addAction(R.drawable.next_button, "NEXT", nextPendingIntent)


        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }

    fun removeNotification() {
        notificationManager.cancel(0)
    }

    fun getPlaying(): Boolean? {
        if (currentSong == null) return null
        return mediaPlayer.isPlaying
    }

    fun getLoop(): Boolean {
        return loop
    }

    fun getRandom(): Boolean {
        return random
    }

    fun getProgress(): Int {
        if (currentSong == null) return -1
        return mediaPlayer.currentPosition / mediaPlayer.duration * 100
    }

    fun getCurrent(): SongFinder.Song? {
        return currentSong
    }

}
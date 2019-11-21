package contre.facon.bruitdansleciel

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Build
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.progur.droidmelody.SongFinder
import contre.facon.bruitdansleciel.adapter.SongsAdapter
import contre.facon.bruitdansleciel.`interface`.SongClickListener
import contre.facon.bruitdansleciel.`interface`.SongsListChangeListner
import contre.facon.bruitdansleciel.helper.SongHelper
import contre.facon.bruitdansleciel.service.Player
import contre.facon.bruitdansleciel.utils.Constants


class MainActivity : Activity(), SongClickListener, SongsListChangeListner {


    private var songsArray: List<SongFinder.Song> = listOf()

    private lateinit var playPauseButton: ImageButton
    private lateinit var loopButton: ImageButton
    private lateinit var randomButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton


    private lateinit var songHelper: SongHelper
    private lateinit var audioPlayer: Player
    private lateinit var notificationManager: NotificationManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Ask for the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.Permission.READ_STORAGE_PERMISSION_REQUEST

            )

        } else {
            initialise()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.Permission.READ_STORAGE_PERMISSION_REQUEST -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initialise()
                }
                return
            }
            else -> {

            }
        }
    }

    private fun initialise() {
        linkButtons()

        songHelper = SongHelper(applicationContext, this)
        val manager = createNotificationChannel()
        if (manager != null) {
            notificationManager = manager
        }
        audioPlayer = Player(this, notificationManager, songsArray, false)

        getAllSongs()

        viewManager = LinearLayoutManager(this)
        viewAdapter = SongsAdapter(songsArray, this)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSongs).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    private fun linkButtons() {
        playPauseButton = findViewById(R.id.play_pause_button)
        loopButton = findViewById(R.id.loop_button)
        randomButton = findViewById(R.id.random_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)

        playPauseButton.setOnClickListener {
            if (audioPlayer.getPlaying() == false) {
                audioPlayer.playPauseSong()
                playPauseButton.setBackgroundResource(R.drawable.pause_button)
            } else {
                audioPlayer.playPauseSong()
                playPauseButton.setBackgroundResource(R.drawable.play_button)
            }
        }

        loopButton.setOnClickListener {
            if (audioPlayer.getLoop() == false) {
                audioPlayer.setLoop()
                loopButton.setBackgroundResource(R.drawable.loop_button_clicked)
            } else {
                audioPlayer.setLoop()
                loopButton.setBackgroundResource(R.drawable.loop_button)
            }
        }

        randomButton.setOnClickListener {
            if (audioPlayer.random == false) {
                randomButton.setBackgroundResource(R.drawable.random_button_clicked)
                audioPlayer.setRandom()
            } else {
                randomButton.setBackgroundResource(R.drawable.random_button)
                audioPlayer.setRandom()
            }
        }
        nextButton.setOnClickListener {
            audioPlayer.playNextSong()
        }

        previousButton.setOnClickListener {
            audioPlayer.playPreviousSong()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (notificationManager is NotificationManager) {
            notificationManager.cancelAll()
        }
    }


    //To retreive all song on the sd card
    private fun getAllSongs() {
        //songHelper.getAllFromDatabase()
        songHelper.scanDeviceMemory(contentResolver)
    }

    override fun onSongListChange(songList: List<SongFinder.Song>) {
        songsArray = songList
        val songAdapter = SongsAdapter(songsArray, this)
        recyclerView.adapter = songAdapter
        audioPlayer.updateSongList(songsArray)
    }

    override fun onSongClick(song: SongFinder.Song) {
        audioPlayer.playSong(song)
        playPauseButton.setBackgroundResource(R.drawable.pause_button)
    }

    private fun createNotificationChannel(): NotificationManager? {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Bruitdansleciel"
            val descriptionText = "Lecture en cours ..."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel("bruitdansleciel_notify_channe", name, importance).apply {
                    description = descriptionText
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channel.setSound(null, null)
            notificationManager.createNotificationChannel(channel)
            return notificationManager
        }
        return null
    }
}

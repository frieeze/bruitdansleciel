package contre.facon.bruitdansleciel

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.progur.droidmelody.SongFinder
import contre.facon.bruitdansleciel.`interface`.PlayerListener
import contre.facon.bruitdansleciel.adapter.SongsAdapter
import contre.facon.bruitdansleciel.`interface`.SongClickListener
import contre.facon.bruitdansleciel.`interface`.SongsListChangeListner
import contre.facon.bruitdansleciel.helper.SongHelper
import contre.facon.bruitdansleciel.service.Player
import contre.facon.bruitdansleciel.utils.Constants
import kotlinx.coroutines.delay
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MainActivity : AppCompatActivity(),SongClickListener, SongsListChangeListner, PlayerListener {


    private var songsArray: List<SongFinder.Song> = listOf()
    private var progressBarHandlerActivated: Boolean = false

    private lateinit var playPauseButton: ImageButton
    private lateinit var loopButton: ImageButton
    private lateinit var randomButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var songInfoText: TextView
    private lateinit var progressBar: ProgressBar


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
        initViewLinks()

        songHelper = SongHelper(applicationContext, this)
        val manager = createNotificationChannel()
        if (manager != null) {
            notificationManager = manager
        }
        audioPlayer = Player(this, this, notificationManager, songsArray)

        getAllSongs()

        viewManager = LinearLayoutManager(this)
        viewAdapter = SongsAdapter(songsArray, this)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSongs).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    private fun initViewLinks() {
        songInfoText = findViewById(R.id.song_info_text)

        playPauseButton = findViewById(R.id.play_pause_button)
        loopButton = findViewById(R.id.loop_button)
        randomButton = findViewById(R.id.random_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        progressBar = findViewById(R.id.progressBar)

        playPauseButton.setOnClickListener {
            if (audioPlayer.getPlaying() == false) {
                audioPlayer.playPauseSong()
                playPauseButton.setBackgroundResource(R.drawable.pause_button)
            } else if (audioPlayer.getPlaying() == true) {
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
            if (audioPlayer.getRandom() == false) {
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

    //To retreive all songs device's memory
    private fun getAllSongs() {
        songHelper.scanDeviceMemory(contentResolver)
    }

    fun progressBarHandler() {
        doAsync {
            do {
                var (current, duration) = audioPlayer.getProgress()

                if (duration == 0) {
                    uiThread {
                        progressBar.progress = 0
                    }
                } else {
                    var ratio: Int = ((current/duration)*100)
                    uiThread { progressBar.progress =  ratio}
                }
                Thread.sleep(200)
            } while (duration != 0)
            progressBarHandlerActivated = false
        }
    }

    override fun onPlaySong(song: SongFinder.Song) {
        songInfoText.text = song.title
        if (!progressBarHandlerActivated) {
            progressBarHandlerActivated = true
            progressBarHandler()
        }

    }

    override fun onPlayPauseButtonChange() {
        if (audioPlayer.getPlaying() == true) {
            playPauseButton.setBackgroundResource(R.drawable.pause_button)
        } else {
            playPauseButton.setBackgroundResource(R.drawable.play_button)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        if (notificationManager is NotificationManager) {
            notificationManager.cancelAll()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.sortByArtist) {
            Toast.makeText(this, "Sort by Artist", Toast.LENGTH_LONG).show()
            return true
        }
        if (id == R.id.sortByAlbum) {
            Toast.makeText(this, "Sort by Album", Toast.LENGTH_LONG).show()
            return true
        }

        return super.onOptionsItemSelected(item)

    }

}

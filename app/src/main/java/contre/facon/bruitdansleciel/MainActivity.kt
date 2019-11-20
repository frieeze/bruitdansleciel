package contre.facon.bruitdansleciel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.net.Uri
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.progur.droidmelody.SongFinder
import contre.facon.bruitdansleciel.Adapter.SongsAdapter
import contre.facon.bruitdansleciel.Interface.Listener


class MainActivity : AppCompatActivity(), Listener {

    private var mediaPlayer: MediaPlayer = MediaPlayer()

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
                0
            )

        }

        val songsArray: List<SongFinder.Song> = getAllSongs()

        viewManager = LinearLayoutManager(this)
        viewAdapter = SongsAdapter(songsArray,this)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSongs).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

/*
        val context: Context = this.applicationContext

        //Place where all our songs are stored
        val pathToSong: String = context.getExternalFilesDir("sdcard/Music/").toString()
        val fileDirectory = File(pathToSong); //The folder w/ every songs

        val nameSong: ArrayList<String> = ArrayList()


        val searchBar: EditText = findViewById(R.id.searchFilter)

        val listView: ListView = findViewById(R.id.listViewSongs) // The list view on the main page
        val adapter = ArrayAdapter<String>(
            this,
            R.layout.listview_layout_songs,
            R.id.name,
            nameSong
        ) // The adatater to link the array of song + the listView
        listView.adapter = adapter

        //To add the searchbar
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.getFilter().filter(p0)
            }

        })

        val mediaPlayer = MediaPlayer()

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                val itemValue = listView.getItemAtPosition(position)
                val u: Uri = itemValue.uri
                if (file.exists()) {
                    println("yesyes")
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(getApplicationContext(), u);
                    mediaPlayer.prepare();
                    play_pause_button.text = "Pause"
                    mediaPlayer.start();
                    val u: Uri = Uri.fromFile(file)

                }
            }


        //val file = File(context.getExternalFilesDir("sdcard/Music/jacky.mp3").toString() )
        //if(file.exists()){
        //    val u: Uri = Uri.fromFile(file)
        //    mediaPlayer = MediaPlayer.create(this, u)
        //}

        //mediaPlayer = MediaPlayer.create(this, u);// Pour jouer un son dans raw
        //mediaPlayer?.start()
        //Toast.makeText(this,"Music playing.",Toast.LENGTH_SHORT).show();

        val playPauseButton = findViewById<Button>(R.id.play_pause_button)

        play_pause_button.setOnClickListener {
            if (play_pause_button.text == "Play") {
                println("yes")
                mediaPlayer?.start()
                playPauseButton.text = "Pause"
            } else if (play_pause_button.text == "Pause") {
                println("no")
                mediaPlayer?.pause()
                mediaPlayer?.seekTo(0)
                playPauseButton.text = "Play"
            }
        }
*/

    }

    private fun playSong(uri: Uri){


        mediaPlayer.reset()
        mediaPlayer.setDataSource(getApplicationContext(), uri);
        mediaPlayer.prepare();
        play_pause_button.text = "Pause"
        mediaPlayer.start();
    }

    //To retreive all song on the sd card
    fun getAllSongs(): List<SongFinder.Song> {
        val songFinder = SongFinder(contentResolver)
        songFinder.prepare()

        return songFinder.allSongs

    }

    override fun onSongClick(song: SongFinder.Song) {
        Log.e("Click", song.title)
        playSong(song.uri)
    }
}

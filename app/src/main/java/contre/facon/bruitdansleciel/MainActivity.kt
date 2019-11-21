package contre.facon.bruitdansleciel

import android.content.Context
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.net.Uri
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.*


class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportActionBar != null)
            supportActionBar?.hide()

        val context: Context = this.applicationContext

        //Place where all our songs are stored
        val pathToSong: String = context.getExternalFilesDir("sdcard/Music/").toString()
        val fileDirectory = File(pathToSong); //The folder w/ every songs

        val nameSong: ArrayList<String> = ArrayList()
        getAllSongs(nameSong)   //ON recupère tous les sons dans le repertoire sdcard/music/


        val searchBar: EditText = findViewById(R.id.searchFilter)

        val listView: ListView = findViewById(R.id.listViewSongs) // The list view on the main page
        val adapter = ArrayAdapter<String>(
            this,
            R.layout.listview_layout_songs,
            R.id.song_name,
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
                // do something...

                println(context.getExternalFilesDir("sdcard/Music/").toString())
                val itemValue = listView.getItemAtPosition(position).toString()
                val file =
                    File(context.getExternalFilesDir("sdcard/Music/").toString() + "/" + itemValue)
                val u: Uri = Uri.fromFile(file)
                if (file.exists()) {
                    println("yesyes")
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(getApplicationContext(), u);
                    mediaPlayer.prepare();
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


        val playPauseButton = findViewById<ImageButton>(R.id.play_pause_button)
        val loopButton = findViewById<ImageButton>(R.id.loop_button)
        val randomButton = findViewById<ImageButton>(R.id.random_button)
        val nextButton = findViewById<ImageButton>(R.id.next_button)
        var isPlayPauseClicked: Boolean? = false
        var isLoopClicked: Boolean? = false
        var isRandomCLicked: Boolean? = false

        playPauseButton.setOnClickListener {
            if (isPlayPauseClicked == false) {
                mediaPlayer?.pause()
                playPauseButton.setBackgroundResource(R.drawable.pause_button)
                isPlayPauseClicked = true
            } else if (isPlayPauseClicked == true) {
                mediaPlayer?.start()
                playPauseButton.setBackgroundResource(R.drawable.play_button)
                isPlayPauseClicked = false
            }
        }

        loopButton.setOnClickListener {
            if (isLoopClicked == false) {
                mediaPlayer.setLooping(true)
                loopButton.setBackgroundResource(R.drawable.loop_button_clicked)
                isLoopClicked = true
            } else if (isLoopClicked == true) {
                mediaPlayer.setLooping(false)
                loopButton.setBackgroundResource(R.drawable.loop_button)
                isLoopClicked = false
            }
        }

        randomButton.setOnClickListener {
            if (isRandomCLicked == false) {
                randomButton.setBackgroundResource(R.drawable.random_button_clicked)
                isRandomCLicked = true
            } else if (isRandomCLicked == true) {
                randomButton.setBackgroundResource(R.drawable.random_button)
                isRandomCLicked = false
            }
        }

        nextButton.setOnClickListener {

        }



        //To retreive all song on the sd card

    }

    fun getAllSongs(nameSong: ArrayList<String>) {
        val context: Context = this.applicationContext
        val pathToSong: String = context.getExternalFilesDir("sdcard/Music/").toString()
        val fileDirectory = File(pathToSong); //The folder w/ every songs
        val reg_mp3: Regex = "(\\/.*\\.+mp3)\$".toRegex() // to spot every .mp3 files



        fileDirectory.walk().forEach {

            if (reg_mp3.containsMatchIn(it.toString())) {
                nameSong.add(it.name.toString())


            }
        }
    }
}

package contre.facon.bruitdansleciel

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.IntegerRes
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.listview_layout_songs.view.*
import org.intellij.lang.annotations.RegExp
import java.io.File
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.Editable
import android.text.TextWatcher
import androidx.room.Entity


class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val context: Context = this.applicationContext

        //Place where all our songs are stored
        val pathToSong: String = context.getExternalFilesDir("sdcard/Music/").toString()
        val fileDirectory = File(pathToSong); //The folder w/ every songs

        val nameSong: ArrayList<String> = ArrayList()
        getAllSongs(nameSong)   //ON recupère tous les sons dans le repertoire sdcard/music/




        val searchBar: EditText = findViewById(R.id.searchFilter)

        val listView: ListView = findViewById(R.id.listViewSongs) // The list view on the main page
        val adapter = ArrayAdapter<String>(this, R.layout.listview_layout_songs,R.id.name, nameSong) // The adatater to link the array of song + the listView
        listView.adapter = adapter

        //To add the searchbar
        searchBar.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.getFilter().filter(p0)
            }

        })

        val mediaPlayer = MediaPlayer()

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            // do something...

            println(context.getExternalFilesDir("sdcard/Music/").toString())
            val itemValue = listView.getItemAtPosition(position).toString()
            val file = File(context.getExternalFilesDir("sdcard/Music/").toString() + "/" + itemValue )
            val u: Uri = Uri.fromFile(file)
            if(file.exists()){
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

        play_pause_button.setOnClickListener{
            if (play_pause_button.text == "Play") {
                println("yes")
                mediaPlayer?.start()
                playPauseButton.text = "Pause"
            }
            else if (play_pause_button.text == "Pause") {
                println("no")
                mediaPlayer?.pause()
                mediaPlayer?.seekTo(0)
                playPauseButton.text = "Play"
            }
        }
        
        
    }

    //To retreive all song on the sd card
    fun getAllSongs(nameSong: ArrayList<String>){
        val context: Context = this.applicationContext
        val pathToSong: String = context.getExternalFilesDir("sdcard/Music/").toString()
        val fileDirectory = File(pathToSong); //The folder w/ every songs
        val reg_mp3: Regex = "(\\/.*\\.+mp3)\$".toRegex() // to spot every .mp3 files



        fileDirectory.walk().forEach {

            if( reg_mp3.containsMatchIn(it.toString())){
                nameSong.add(it.name.toString())


            }
        }
    }
}

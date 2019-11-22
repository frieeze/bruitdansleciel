package contre.facon.bruitdansleciel.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import contre.facon.bruitdansleciel.R
import contre.facon.bruitdansleciel.`interface`.PlayerListener
import contre.facon.bruitdansleciel.service.Player

class SongPlayerFragment() : Fragment() {

    private lateinit var mListener: PlayerListener

    private lateinit var player: Player
    private lateinit var albumPicture: ImageView
    private lateinit var songName: TextView
    private lateinit var artistName: TextView
    private lateinit var albumName: TextView

    private lateinit var playPauseButton: ImageButton
    private lateinit var loopButton: ImageButton
    private lateinit var randomButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        mListener = activity as PlayerListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.song_lecture_layout, container, false)
        albumPicture = view.findViewById(R.id.song_picture)
        songName = view.findViewById(R.id.lecture_layout_songName)
        albumName = view.findViewById(R.id.lecture_layout_albumName)
        artistName = view.findViewById(R.id.lecture_layout_artisteName)

        playPauseButton = view.findViewById(R.id.lecture_layout_playpause_button)
        loopButton = view.findViewById(R.id.lecture_layout_loop_button)
        randomButton = view.findViewById(R.id.lecture_layout_random_button)
        nextButton = view.findViewById(R.id.lecture_layout_next_button)
        previousButton = view.findViewById(R.id.lecture_layout_previous_button)


        playPauseButton.setOnClickListener {
            if (player.getPlaying() == false) {
                player.playPauseSong()
                playPauseButton.setBackgroundResource(R.drawable.pause_button)
            } else if (player.getPlaying() == true) {
                player.playPauseSong()
                playPauseButton.setBackgroundResource(R.drawable.play_button)
            }
            mListener.syncWithExtended()
        }

        loopButton.setOnClickListener {
            if (player.getLoop() == false) {
                player.setLoop()
                loopButton.setBackgroundResource(R.drawable.loop_button_clicked)
            } else {
                player.setLoop()
                loopButton.setBackgroundResource(R.drawable.loop_button)
            }
            mListener.syncWithExtended()
        }

        randomButton.setOnClickListener {
            if (player.getRandom() == false) {
                randomButton.setBackgroundResource(R.drawable.random_button_clicked)
                player.setRandom()
            } else {
                randomButton.setBackgroundResource(R.drawable.random_button)
                player.setRandom()
            }
            mListener.syncWithExtended()
        }
        nextButton.setOnClickListener {
            player.playNextSong()
            handleSongChange()
        }

        previousButton.setOnClickListener {
            player.playPreviousSong()
            handleSongChange()
        }
        handleSongChange()
        syncButtonWithMain()
        view.findViewById<RelativeLayout>(R.id.lecture_layout_outOfBounds).setOnClickListener {
            fragmentManager?.popBackStack()
        }
        return view
    }

    fun handleSongChange() {
        val song = player.getCurrent()
        if (song == null) {
            fragmentManager?.popBackStack()
        }
        song?.let {
            songName.text = song.title
            artistName.text = song.artist
            albumName.text = song.album
            albumPicture.setImageURI(song.albumArt)
        }
    }

    fun syncButtonWithMain() {
        val playing = player.getPlaying()
        playing?.let {
            if (playing) {
                playPauseButton.setBackgroundResource(R.drawable.pause_button)
            }
        }
        if (player.getLoop()) {
            loopButton.setBackgroundResource(R.drawable.loop_button_clicked)
        }
        if (player.getRandom()) {
            randomButton.setBackgroundResource(R.drawable.random_button_clicked)
        }
    }


    fun getPlayer(mediaPlayer: Player) {
        player = mediaPlayer
    }
}

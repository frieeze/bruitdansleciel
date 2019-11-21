package contre.facon.bruitdansleciel.`interface`

import com.progur.droidmelody.SongFinder

interface PlayerListener {
    fun onPlayPauseButtonChange()
    fun onPlaySong(song: SongFinder.Song)
    fun onNotifyClick(message: String)
}
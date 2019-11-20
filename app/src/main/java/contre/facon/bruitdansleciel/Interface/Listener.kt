package contre.facon.bruitdansleciel.Interface

import com.progur.droidmelody.SongFinder

interface Listener {
    fun onSongClick(song: SongFinder.Song)
}
package contre.facon.bruitdansleciel.`interface`

import com.progur.droidmelody.SongFinder

interface SongClickListener {
    fun onSongClick(song: SongFinder.Song)
}
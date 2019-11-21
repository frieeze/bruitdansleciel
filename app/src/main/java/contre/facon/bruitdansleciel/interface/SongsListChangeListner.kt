package contre.facon.bruitdansleciel.`interface`

import com.progur.droidmelody.SongFinder

interface SongsListChangeListner {
    fun onSongListChange(songList: List<SongFinder.Song>)
}
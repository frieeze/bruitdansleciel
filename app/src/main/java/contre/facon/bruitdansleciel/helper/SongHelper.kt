package contre.facon.bruitdansleciel.helper

import android.content.ContentResolver
import android.content.Context
import com.progur.droidmelody.SongFinder
import contre.facon.bruitdansleciel.`interface`.SongsListChangeListner
import contre.facon.bruitdansleciel.db.SongDBHelper
import contre.facon.bruitdansleciel.db.SongsDb
import org.jetbrains.anko.doAsync

class SongHelper(val context: Context, val mListener: SongsListChangeListner) {
    private val database by lazy { SongsDb(SongDBHelper(context)) }
    fun getAllFromDatabase() {
        doAsync {
            val list = database.getAllSongs()
            mListener.onSongListChange(list)
        }
    }

    fun scanDeviceMemory(contentResolver: ContentResolver) {
        doAsync {
            val songFinder = SongFinder(contentResolver)
            songFinder.prepare()
            database.saveMultipleSongs(songFinder.allSongs)
            getAllFromDatabase()
        }

    }
}
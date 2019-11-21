package contre.facon.bruitdansleciel.db

import com.progur.droidmelody.SongFinder
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insertOrThrow
import org.jetbrains.anko.db.select


class SongsDb(private val dbHelper: SongDBHelper) {
    fun getAllSongs() = dbHelper.use {
        select(SongListTable.NAME).parseList(classParser<SongFinder.Song>())
    }

    fun saveSong(song: SongFinder.Song) = dbHelper.use {
        insertOrThrow(
            SongListTable.NAME,
            SongListTable.ID to song.id,
            SongListTable.TITLE to song.title,
            SongListTable.ALBUM to song.album,
            SongListTable.ARTIST to song.artist,
            SongListTable.DURATION to song.duration,
            SongListTable.ALBUMID to song.albumId

        )
    }

    fun saveMultipleSongs(songs: List<SongFinder.Song>) {
        for (s in songs) {
            saveSong(s)
        }
    }
}
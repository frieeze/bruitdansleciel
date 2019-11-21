package contre.facon.bruitdansleciel.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class SongDBHelper(val context: Context) :
    ManagedSQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_NAME = "songs.db"
        val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            SongListTable.NAME,
            true,
            SongListTable.ID to INTEGER + PRIMARY_KEY,
            SongListTable.ARTIST to TEXT,
            SongListTable.TITLE to TEXT,
            SongListTable.ALBUM to TEXT,
            SongListTable.DURATION to INTEGER,
            SongListTable.ALBUMID to INTEGER
            )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(SongListTable.NAME, true)
        onCreate(db)
    }
}

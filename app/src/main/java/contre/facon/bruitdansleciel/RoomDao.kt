package contre.facon.bruitdansleciel

import androidx.room.*


@Dao
interface NoteDao {
    @Query("SELECT * FROM songs")
    suspend fun getNotes(): List<RoomNote>

    @Query("SELECT * FROM songs WHERE songname = :creationDate")
    suspend fun getNoteById(creationDate: String): RoomNote

    @Delete
    suspend fun deleteNote(note: RoomNote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNote(note: RoomNote): Long
}


//https://www.youtube.com/watch?v=CcaCpRCACzU
package contre.facon.bruitdansleciel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "songs",
    indices = [Index("songname")]
)
data class RoomNote(
    @PrimaryKey
    @ColumnInfo(name = "songname")
    val songname: String


)
package contre.facon.bruitdansleciel.db

import android.net.Uri
import com.progur.droidmelody.SongFinder
import java.net.URI

data class SongData(
    val title: String,
    val artist: String,
    val album: String,
    val id: Long,
    val albumId: Long,
    val duration: Long

)
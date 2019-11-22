package contre.facon.bruitdansleciel.utils

class Constants {
    class Permission {
        companion object {
            const val READ_STORAGE_PERMISSION_REQUEST: Int = 0
        }
    }

    class Intents {
        companion object {
            const val CF_PLAYER_PAUSE: String = "CF_PLAYER_PAUSE"
            const val CF_PLAYER_NEXT: String = "CF_PLAYER_NEXT"
            const val CF_PLAYER_PREVIOUS: String = "CF_PLAYER_PREVIOUS"
        }
    }

    class Song {
        companion object {
            const val TITLE: String = "title"
            const val ARTIST: String = "artist"
            const val ALBUM: String = "album"
            const val ID: String = "id"
            const val ALBUMID: String = "albumid"
            const val DURATION: String = "duration"
        }
    }
}
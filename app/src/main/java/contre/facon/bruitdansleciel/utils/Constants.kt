package contre.facon.bruitdansleciel.utils

class Constants {
    class Permission {
        companion object {
            const val READ_STORAGE_PERMISSION_REQUEST: Int = 0
        }
    }
    class Intents{
        companion object{
            const val CF_PLAYER_PAUSE: String = "contre.facon.bruitdansleciel.CF_PLAYER_PAUSE"
            const val CF_PLAYER_NEXT: String = "CF_PLAYER_NEXT"
            const val CF_PLAYER_PREVIOUS: String = "CF_PLAYER_PREVIOUS"
        }
    }
}
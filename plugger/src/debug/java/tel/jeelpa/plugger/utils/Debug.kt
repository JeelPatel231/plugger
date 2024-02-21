package tel.jeelpa.plugger.utils

import android.util.Log

fun dbg(vararg any: Any, prefix: String? = null) {
    val newPrefix = if(prefix != null) "$prefix:" else ""
    Log.d("DEBUG", newPrefix + any.joinToString(" | "))
}

fun <T : Any> T.dbg(prefix: String? = null) = also {
    dbg(this, prefix = prefix)
}

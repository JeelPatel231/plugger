package tel.jeelpa.plugger

import android.content.Context
import dalvik.system.DexClassLoader
import kotlinx.serialization.json.Json

fun Context.getClassLoader(path: String): DexClassLoader {
    return DexClassLoader(
        path,
        cacheDir.absolutePath,
        null,
        classLoader
    )
}

inline fun <reified T> String.parsed(): T {
    return Json.decodeFromString<T>(this)
}

fun <T : Any> ((Exception) -> Unit).tryWith(block: () -> T?): T? {
    return try {
        block()
    } catch (e: Exception) {
        this.invoke(e)
        null
    }
}
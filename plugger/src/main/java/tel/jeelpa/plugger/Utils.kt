package tel.jeelpa.plugger

import kotlinx.serialization.json.Json

inline fun <reified T> String.parsed(): T {
    return Json.decodeFromString<T>(this)
}

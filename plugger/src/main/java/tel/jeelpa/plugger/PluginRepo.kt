package tel.jeelpa.plugger

import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface PluginRepo<TPlugin> {
    fun getAllPlugins(exceptionHandler: (Exception) -> Unit): Flow<List<TPlugin>>
}
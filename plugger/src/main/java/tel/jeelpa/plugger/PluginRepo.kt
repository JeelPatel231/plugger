package tel.jeelpa.plugger

import kotlinx.coroutines.flow.StateFlow

interface PluginRepo<TMetadata, TPlugin> {
    fun getAllPlugins(): StateFlow<List<Result<Pair<TMetadata, TPlugin>>>>
}
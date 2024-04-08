package tel.jeelpa.plugger

import kotlinx.coroutines.flow.Flow

interface PluginRepo<TMetadata, TPlugin> {
    fun getAllPlugins(): Flow<List<Result<Pair<TMetadata, TPlugin>>>>
}
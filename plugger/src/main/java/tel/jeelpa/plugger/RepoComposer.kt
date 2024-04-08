package tel.jeelpa.plugger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class RepoComposer<TMetadata, TPlugin>(private vararg val repos: PluginRepo<TMetadata, TPlugin>) : PluginRepo<TMetadata, TPlugin> {
    override fun getAllPlugins(): Flow<List<Result<Pair<TMetadata, TPlugin>>>> =
        combine(
            repos.map { it.getAllPlugins() }
        ) { array ->
            array.reduce { a, b -> a + b }
        }
}

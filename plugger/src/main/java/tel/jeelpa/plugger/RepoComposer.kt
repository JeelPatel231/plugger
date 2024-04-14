package tel.jeelpa.plugger

import kotlinx.coroutines.flow.StateFlow
import tel.jeelpa.plugger.utils.combineStates

class RepoComposer<TMetadata, TPlugin>(private vararg val repos: PluginRepo<TMetadata, TPlugin>) : PluginRepo<TMetadata, TPlugin> {
    override fun getAllPlugins(): StateFlow<List<Result<Pair<TMetadata, TPlugin>>>> =
        repos.map { it.getAllPlugins() }
            .reduce { a, b -> combineStates(a,b) { x, y -> x + y } }
}

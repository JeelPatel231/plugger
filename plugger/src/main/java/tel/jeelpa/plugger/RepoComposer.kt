package tel.jeelpa.plugger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.lang.Exception

class RepoComposer<TPlugin>(private vararg val repos: PluginRepo<TPlugin>) : PluginRepo<TPlugin> {
    override fun getAllPlugins(exceptionHandler: (Exception) -> Unit): Flow<List<TPlugin>> =
        combine(
            repos.map { it.getAllPlugins(exceptionHandler) }
        ) { array ->
            array.reduce { a, b -> a + b }
        }
}

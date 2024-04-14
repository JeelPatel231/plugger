package tel.jeelpa.plugger

import kotlinx.coroutines.flow.StateFlow
import tel.jeelpa.plugger.utils.mapState

data class PluginRepoImpl<TSourceInput, TMetadata, TPlugin>(
    private val pluginSource: PluginSource<TSourceInput>,
    private val manifestParser: ManifestParser<TSourceInput, TMetadata>,
    private val pluginLoader: PluginLoader<TMetadata, TPlugin>
) : PluginRepo<TMetadata, TPlugin> {
    override fun getAllPlugins(): StateFlow<List<Result<Pair<TMetadata, TPlugin>>>> =
        pluginSource.getSourceFiles().mapState { files ->
                files.map {
                    runCatching { manifestParser.parseManifest(it) }
                }
            }.mapState { metadata ->
                metadata.map { resultMetadata ->
                    resultMetadata.mapCatching {
                        Pair(it, pluginLoader.loadPlugin(it))
                    }
                }
            }
}
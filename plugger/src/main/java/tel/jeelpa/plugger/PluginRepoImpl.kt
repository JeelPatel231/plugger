package tel.jeelpa.plugger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class PluginRepoImpl<TSourceInput, TMetadata, TPlugin>(
    private val pluginSource: PluginSource<TSourceInput>,
    private val manifestParser: ManifestParser<TSourceInput, TMetadata>,
    private val pluginLoader: PluginLoader<TMetadata, TPlugin>
) : PluginRepo<TMetadata, TPlugin> {
    override fun getAllPlugins(): Flow<List<Result<Pair<TMetadata, TPlugin>>>> =
        pluginSource.getSourceFiles().map { files ->
                files.map {
                    runCatching { manifestParser.parseManifest(it) }
                }
            }.map { metadata ->
                metadata.map { resultMetadata ->
                    resultMetadata.mapCatching {
                        Pair(it, pluginLoader.loadPlugin(it))
                    }
                }
            }
}
package tel.jeelpa.plugger.pluginloader.file

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tel.jeelpa.plugger.ManifestParser
import tel.jeelpa.plugger.PluginLoader
import tel.jeelpa.plugger.PluginRepo
import tel.jeelpa.plugger.pluginloader.AndroidPluginLoader
import java.io.File


class FileSystemPluginLoader<TPlugin>(
    private val context: Context,
    private val config: FilePluginConfig,
    private val loader: PluginLoader<TPlugin> = AndroidPluginLoader(context),
    private val manifestParser: ManifestParser<String> = FilePluginManifestParser(context)
) : PluginRepo<TPlugin> {

    private fun loadAllPlugins(): List<Result<TPlugin>> {
        return (File(config.path).listFiles() ?: emptyArray<File>())
            .map { it.path }
            .filter { it.endsWith(config.extension) }
            .map { runCatching { manifestParser.parseManifest(it) } }
            .map { runCatching { loader(it.getOrThrow()) } }
    }

    // TODO: Listen for filesystem change broadcasts and update flow on change
    override fun getAllPlugins(): Flow<List<Result<TPlugin>>> {
        return flowOf(loadAllPlugins())
    }
}
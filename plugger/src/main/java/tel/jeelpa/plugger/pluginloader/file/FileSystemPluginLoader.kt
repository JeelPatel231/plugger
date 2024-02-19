package tel.jeelpa.plugger.pluginloader.file

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tel.jeelpa.plugger.PluginLoader
import tel.jeelpa.plugger.PluginRepo
import tel.jeelpa.plugger.pluginloader.AndroidPluginLoader
import java.io.File


class FileSystemPluginLoader<TPlugin>(
    private val context: Context,
    private val config: FilePluginConfig,
    private val loader: PluginLoader = AndroidPluginLoader(context),
) : PluginRepo<TPlugin> {

    private val filePluginManifestParser = FilePluginManifestParser(context)

    private fun loadAllPlugins(): List<TPlugin> {
        return (File(config.path, "plugins").listFiles() ?: emptyArray<File>())
            .map { it.path }
            .filter { it.endsWith(config.extension) }
            .map { filePluginManifestParser.parseManifest(it) }
            .map { loader(it) }
    }

    // TODO: Listen for filesystem change broadcasts and update flow on change
    override fun getAllPlugins(): Flow<List<TPlugin>> {
        return flowOf(loadAllPlugins())
    }
}
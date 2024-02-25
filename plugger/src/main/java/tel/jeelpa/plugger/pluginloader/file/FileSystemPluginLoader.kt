package tel.jeelpa.plugger.pluginloader.file

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tel.jeelpa.plugger.ManifestParser
import tel.jeelpa.plugger.PluginLoader
import tel.jeelpa.plugger.PluginRepo
import tel.jeelpa.plugger.pluginloader.AndroidPluginLoader
import tel.jeelpa.plugger.tryWith
import java.io.File
import java.lang.Exception


class FileSystemPluginLoader<TPlugin>(
    private val context: Context,
    private val config: FilePluginConfig,
    private val loader: PluginLoader = AndroidPluginLoader(context),
    private val manifestParser: ManifestParser<String> = FilePluginManifestParser(context)
) : PluginRepo<TPlugin> {

    private fun loadAllPlugins(exceptionHandler: (Exception) -> Unit): List<TPlugin> {
        return (File(config.path, "plugins").listFiles() ?: emptyArray<File>())
            .map { it.path }
            .filter { it.endsWith(config.extension) }
            .mapNotNull {
                exceptionHandler.tryWith {
                    manifestParser.parseManifest(it)
                }
            }
            .mapNotNull {
                exceptionHandler.tryWith {
                    loader<TPlugin>(it)
                }
            }
    }

    // TODO: Listen for filesystem change broadcasts and update flow on change
    override fun getAllPlugins(exceptionHandler: (Exception) -> Unit): Flow<List<TPlugin>> {
        return flowOf(loadAllPlugins(exceptionHandler))
    }
}
package tel.jeelpa.plugger.pluginloader.file

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tel.jeelpa.plugger.ManifestParser
import tel.jeelpa.plugger.PluginLoader
import tel.jeelpa.plugger.PluginRepo
import tel.jeelpa.plugger.pluginloader.AndroidPluginLoader
import tel.jeelpa.plugger.pluginloader.GetClassLoaderWithPathUseCase
import java.io.File


class FileSystemPluginLoader<TPlugin>(
    getClassLoader: GetClassLoaderWithPathUseCase,
    private val config: FilePluginConfig,
    private val loader: PluginLoader = AndroidPluginLoader(getClassLoader),
    private val manifestParser: ManifestParser<String> = FilePluginManifestParser(getClassLoader)
) : PluginRepo<TPlugin> {

    constructor(
        context: Context,
        config: FilePluginConfig,
        loader: PluginLoader = AndroidPluginLoader(context),
        manifestParser: ManifestParser<String> = FilePluginManifestParser(context)
    ): this(GetClassLoaderWithPathUseCase(context.classLoader), config, loader, manifestParser)

    private fun loadAllPlugins(): List<TPlugin> {
        return (File(config.path, "plugins").listFiles() ?: emptyArray<File>())
            .map { it.path }
            .filter { it.endsWith(config.extension) }
            .map { manifestParser.parseManifest(it) }
            .map { loader(it) }
    }

    // TODO: Listen for filesystem change broadcasts and update flow on change
    override fun getAllPlugins(): Flow<List<TPlugin>> {
        return flowOf(loadAllPlugins())
    }
}
package tel.jeelpa.plugger.pluginloader.file

import android.content.Context
import android.os.Build
import android.os.FileObserver
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import tel.jeelpa.plugger.ManifestParser
import tel.jeelpa.plugger.PluginLoader
import tel.jeelpa.plugger.PluginRepo
import tel.jeelpa.plugger.pluginloader.AndroidPluginLoader
import tel.jeelpa.plugger.pluginloader.GetClassLoaderWithPathUseCase
import java.io.File


@RequiresApi(Build.VERSION_CODES.Q)
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

    private val folder = File(config.path)

    private fun loadAllPlugins(): List<TPlugin> {
        return folder.listFiles()!!
            .map { it.path }
            .filter { it.endsWith(config.extension) }
            .map { manifestParser.parseManifest(it) }
            .map { loader(it) }
    }

    private val pluginStateFlow = MutableStateFlow(loadAllPlugins())

    private val fsEventsListener = object: FileObserver(File(config.path)) {
        override fun onEvent(event: Int, path: String?) {
            pluginStateFlow.value = loadAllPlugins()
        }
    }

    init { fsEventsListener.startWatching() }

    override fun getAllPlugins(): Flow<List<TPlugin>> =
        pluginStateFlow.asStateFlow()
}
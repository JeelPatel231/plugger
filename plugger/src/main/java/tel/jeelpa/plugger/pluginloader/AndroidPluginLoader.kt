package tel.jeelpa.plugger.pluginloader

import android.content.Context
import tel.jeelpa.plugger.PluginLoader
import tel.jeelpa.plugger.models.PluginMetadata

/**
 * Android Plugin Loader needs at least 2 parameters to load a plugin
 * 1. Path to dex/jar/apk
 * 2. ClassName to be loaded from the file
 *
 * thus anyone using this class must implement PluginMetadata interface,
 * which enforces these 2 variables to be defined
 */
class AndroidPluginLoader<TMetadata: PluginMetadata, TPlugin>(
    private val getClassLoader: GetClassLoaderWithPathUseCase
) : PluginLoader<TMetadata, TPlugin> {
    constructor(context: Context): this(GetClassLoaderWithPathUseCase(context.classLoader))

    // for loading classes.dex from dex, jar or apk
    override fun loadPlugin(pluginMetadata: TMetadata): TPlugin {
        return getClassLoader.getWithPath(pluginMetadata.path)
            .loadClass(pluginMetadata.className)
            .getConstructor()
            .newInstance() as TPlugin
    }
}

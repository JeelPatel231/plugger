package tel.jeelpa.plugger.pluginloader

import android.content.Context
import tel.jeelpa.plugger.PluginLoader
import tel.jeelpa.plugger.models.PluginMetadata

class AndroidPluginLoader(
    private val getClassLoader: GetClassLoaderWithPathUseCase
) : PluginLoader {
    constructor(context: Context): this(GetClassLoaderWithPathUseCase(context.classLoader))

    // for loading classes.dex from dex, jar or apk
    override operator fun <TPlugin> invoke(pluginMetadata: PluginMetadata): TPlugin {
        return getClassLoader.getWithPath(pluginMetadata.path)
            .loadClass(pluginMetadata.className)
            .getConstructor()
            .newInstance() as TPlugin
    }
}

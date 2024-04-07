package tel.jeelpa.plugger.pluginloader

import android.content.Context
import tel.jeelpa.plugger.PluginLoader
import tel.jeelpa.plugger.models.PluginMetadata

class AndroidPluginLoader<TPlugin>(
    private val getClassLoader: GetClassLoaderWithPathUseCase
) : PluginLoader<TPlugin> {
    constructor(context: Context): this(GetClassLoaderWithPathUseCase(context.classLoader))

    // for loading classes.dex from dex, jar or apk
    override operator fun invoke(pluginMetadata: PluginMetadata): TPlugin {
        return getClassLoader.getWithPath(pluginMetadata.path)
            .loadClass(pluginMetadata.className)
            .getConstructor()
            .newInstance() as TPlugin
    }
}

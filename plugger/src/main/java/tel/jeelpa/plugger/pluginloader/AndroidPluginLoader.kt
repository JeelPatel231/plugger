package tel.jeelpa.plugger.pluginloader

import android.content.Context
import tel.jeelpa.plugger.PluginLoader
import tel.jeelpa.plugger.getClassLoader
import tel.jeelpa.plugger.models.PluginMetadata

class AndroidPluginLoader<TPlugin>(private val context: Context) : PluginLoader<TPlugin> {

    // for loading classes.dex from dex, jar or apk
    override operator fun invoke(pluginMetadata: PluginMetadata): TPlugin {

        return context.getClassLoader(pluginMetadata.path)
            .loadClass(pluginMetadata.className)
            .getConstructor()
            .newInstance() as TPlugin
    }
}

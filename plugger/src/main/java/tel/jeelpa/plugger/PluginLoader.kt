package tel.jeelpa.plugger

interface PluginLoader<TMetadata, TPlugin> {
    fun loadPlugin(pluginMetadata: TMetadata): TPlugin
}
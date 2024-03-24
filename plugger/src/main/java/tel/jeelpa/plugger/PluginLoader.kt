package tel.jeelpa.plugger

import tel.jeelpa.plugger.models.PluginMetadata

interface PluginLoader<TPlugin> {
    operator fun invoke(pluginMetadata: PluginMetadata): TPlugin
}
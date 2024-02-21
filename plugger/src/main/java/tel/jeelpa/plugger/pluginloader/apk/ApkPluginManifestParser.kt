package tel.jeelpa.plugger.pluginloader.apk

import android.content.pm.ApplicationInfo
import tel.jeelpa.plugger.ManifestParser
import tel.jeelpa.plugger.models.PluginMetadata

class ApkPluginManifestParser(
    private val pluginConfig: ApkPluginConfiguration
) : ManifestParser<ApplicationInfo> {
    override fun parseManifest(data: ApplicationInfo): PluginMetadata {
        return PluginMetadata(
            path = data.sourceDir,
            className = data.metaData.getString(pluginConfig.metadataSourceClassTag)
                ?: error("ClassName not found in Metadata"),
        )
    }
}

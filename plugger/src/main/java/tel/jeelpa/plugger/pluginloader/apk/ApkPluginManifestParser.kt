package tel.jeelpa.plugger.pluginloader.apk

import android.content.pm.ApplicationInfo
import tel.jeelpa.plugger.ManifestParser
import tel.jeelpa.plugger.models.PluginMetadata
import tel.jeelpa.plugger.models.PluginMetadataImpl

class ApkPluginManifestParser(
    private val metadataSourceClassTag: String
) : ManifestParser<ApplicationInfo, PluginMetadata> {
    override fun parseManifest(data: ApplicationInfo): PluginMetadata {
        return PluginMetadataImpl(
            path = data.sourceDir,
            className = data.metaData.getString(metadataSourceClassTag)
                ?: error("ClassName not found in Metadata"),
        )
    }
}

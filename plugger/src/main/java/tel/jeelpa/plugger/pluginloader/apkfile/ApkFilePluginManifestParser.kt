package tel.jeelpa.plugger.pluginloader.apkfile

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import tel.jeelpa.plugger.ManifestParser
import tel.jeelpa.plugger.models.PluginMetadata
import tel.jeelpa.plugger.pluginloader.apk.ApkPluginConfiguration

class ApkFilePluginManifestParser(
    private val appContext: Context,
    private val pluginConfig: ApkPluginConfiguration
) : ManifestParser<String> {
    companion object {

        @Suppress("Deprecation")
        val PACKAGE_FLAGS = PackageManager.GET_CONFIGURATIONS or
                PackageManager.GET_META_DATA or
                PackageManager.GET_SIGNATURES or
                (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) PackageManager.GET_SIGNING_CERTIFICATES else 0)
    }

    override fun parseManifest(/* archive path */ data: String): PluginMetadata {
        val appInfo = appContext.packageManager
            .getPackageArchiveInfo(data, PACKAGE_FLAGS)
            ?.applicationInfo
            ?: error("Invalid package, cannot get archive info!")

        val className = appInfo.metaData.getString(pluginConfig.metadataSourceClassTag)
            ?: error("ClassName not found in Metadata")

        return PluginMetadata(
            path = data,
            className = className
        )
    }
}

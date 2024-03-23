package tel.jeelpa.plugger.pluginloader.apk

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tel.jeelpa.plugger.ManifestParser
import tel.jeelpa.plugger.PluginLoader
import tel.jeelpa.plugger.PluginRepo
import tel.jeelpa.plugger.pluginloader.AndroidPluginLoader


class ApkPluginLoader<TPlugin>(
    private val packageManager: PackageManager,
    private val configuration: ApkPluginConfiguration,
    private val loader: PluginLoader,
    private val manifestParser: ManifestParser<ApplicationInfo> = ApkPluginManifestParser(configuration),
) : PluginRepo<TPlugin> {

    constructor(
        context: Context,
        configuration: ApkPluginConfiguration,
        loader: PluginLoader = AndroidPluginLoader(context)
    ): this(context.packageManager, configuration, loader)

    companion object {

        @Suppress("Deprecation")
        val PACKAGE_FLAGS = PackageManager.GET_CONFIGURATIONS or
                PackageManager.GET_META_DATA or
                PackageManager.GET_SIGNATURES or
                (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) PackageManager.GET_SIGNING_CERTIFICATES else 0)

    }

    private fun getStaticPlugins(): List<TPlugin> {
        return packageManager.getInstalledPackages(PACKAGE_FLAGS)
            .filter {
                it.reqFeatures.orEmpty().any { featureInfo ->
                    featureInfo.name == configuration.featureName
                }
            }
            .map { manifestParser.parseManifest(it.applicationInfo) }
            .map { loader<TPlugin>(it) }
            .toList()
    }

    // TODO: Listen for app installation broadcasts and update flow on change
    override fun getAllPlugins(): Flow<List<TPlugin>> =
        flowOf(getStaticPlugins())
}
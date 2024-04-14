package tel.jeelpa.plugger.pluginloader.apk

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tel.jeelpa.plugger.PluginSource


class ApkPluginSource(
    private val packageManager: PackageManager,
    private val featureName: String,
) : PluginSource<ApplicationInfo> {

    constructor(
        context: Context,
        featureName: String,
    ): this(context.packageManager, featureName)

    companion object {

        @Suppress("Deprecation")
        val PACKAGE_FLAGS = PackageManager.GET_CONFIGURATIONS or
                PackageManager.GET_META_DATA or
                PackageManager.GET_SIGNATURES or
                (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) PackageManager.GET_SIGNING_CERTIFICATES else 0)

    }

    private fun getStaticPackages(): List<ApplicationInfo> {
        return packageManager.getInstalledPackages(PACKAGE_FLAGS)
            .filter {
                it.reqFeatures.orEmpty().any { featureInfo ->
                    featureInfo.name == featureName
                }
            }.map { it.applicationInfo }
    }

    private val loadedPlugins = MutableStateFlow(getStaticPackages())

    // TODO: Listen for app installation broadcasts and update flow on change
    override fun getSourceFiles(): StateFlow<List<ApplicationInfo>> =
        loadedPlugins.asStateFlow()
}
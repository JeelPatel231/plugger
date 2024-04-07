package tel.jeelpa.plugger.pluginloader.apk

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tel.jeelpa.plugger.PluginSource


class ApkPluginSource(
    private val packageManager: PackageManager,
    private val featureName: String,
) : PluginSource<PackageInfo> {

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

    private fun getStaticPackages(): List<PackageInfo> {
        return packageManager.getInstalledPackages(PACKAGE_FLAGS)
            .filter {
                it.reqFeatures.orEmpty().any { featureInfo ->
                    featureInfo.name == featureName
                }
            }
    }

    // TODO: Listen for app installation broadcasts and update flow on change
    override fun getSourceFiles(): Flow<List<PackageInfo>> =
        flowOf(getStaticPackages())
}
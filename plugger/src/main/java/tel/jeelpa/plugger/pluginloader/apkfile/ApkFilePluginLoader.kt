package tel.jeelpa.plugger.pluginloader.apkfile

import android.content.Context
import kotlinx.coroutines.flow.Flow
import tel.jeelpa.plugger.ManifestParser
import tel.jeelpa.plugger.PluginLoader
import tel.jeelpa.plugger.PluginRepo
import tel.jeelpa.plugger.pluginloader.AndroidPluginLoader
import tel.jeelpa.plugger.pluginloader.file.FileSystemPluginLoader

class ApkFilePluginLoader<TPlugin>(
    private val context: Context,
    private val config: ApkFilePluginConfiguration,
    private val loader: PluginLoader = AndroidPluginLoader(context),
    private val manifestParser: ManifestParser<String> = ApkFilePluginManifestParser(context, config.toApkPluginConfig())
) : PluginRepo<TPlugin> {

    // delegate the loading work to FileSystemLoader,
    // since reading is same, only parsing differs
    // here we parse Manifest.xml instead of manifest.json
    private val filePluginLoader = FileSystemPluginLoader<TPlugin>(
        context = context,
        loader =  loader,
        config = config.toFileConfig(),
        manifestParser = manifestParser,
    )

    override fun getAllPlugins(): Flow<List<TPlugin>> =
        filePluginLoader.getAllPlugins()

}

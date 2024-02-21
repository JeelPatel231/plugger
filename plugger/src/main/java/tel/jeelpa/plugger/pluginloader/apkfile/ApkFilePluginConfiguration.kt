package tel.jeelpa.plugger.pluginloader.apkfile

import tel.jeelpa.plugger.pluginloader.apk.ApkPluginConfiguration
import tel.jeelpa.plugger.pluginloader.file.FilePluginConfig

data class ApkFilePluginConfiguration(
    val folderPath: String,
    val extension: String,
    val packagePrefix: String,
    val featureName: String = "$packagePrefix.extension",
    val metadataSourceClassTag: String = "$packagePrefix.sourceclass",
) {
    fun toApkPluginConfig() = ApkPluginConfiguration(
        packagePrefix = packagePrefix,
        featureName = featureName,
        metadataSourceClassTag = metadataSourceClassTag,
    )

    fun toFileConfig() = FilePluginConfig(
        path = folderPath,
        extension = extension,
    )
}
package tel.jeelpa.plugger.pluginloader.apk

data class ApkPluginConfiguration(
    val packagePrefix: String,
    val featureName: String = "$packagePrefix.extension",
    val metadataSourceClassTag: String = "$packagePrefix.sourceclass",
)

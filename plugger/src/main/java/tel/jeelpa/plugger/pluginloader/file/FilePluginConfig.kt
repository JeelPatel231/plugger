package tel.jeelpa.plugger.pluginloader.file


data class FilePluginConfig(
    val path: String,
    val extension: String, // to be used as ".example" / ".jar" / ".zip"
)

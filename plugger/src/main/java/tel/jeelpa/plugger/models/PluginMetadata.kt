package tel.jeelpa.plugger.models

interface PluginMetadata {
    val className: String
    val path: String
}

data class PluginMetadataImpl(
    override val className: String,
    override val path: String,
): PluginMetadata

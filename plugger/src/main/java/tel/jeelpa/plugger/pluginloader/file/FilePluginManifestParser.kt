package tel.jeelpa.plugger.pluginloader.file

import android.content.Context
import kotlinx.serialization.Serializable
import tel.jeelpa.plugger.ManifestParser
import tel.jeelpa.plugger.models.PluginMetadata
import tel.jeelpa.plugger.parsed
import tel.jeelpa.plugger.pluginloader.GetClassLoaderWithPathUseCase

class FilePluginManifestParser(
    private val getClassLoader: GetClassLoaderWithPathUseCase
) : ManifestParser<String> {

    constructor(context: Context): this(GetClassLoaderWithPathUseCase(context.classLoader))

    @Serializable
    data class FilePluginManifest(
        val className: String,
    )

    override fun parseManifest(data: String): PluginMetadata {
        val manifestData = getClassLoader.getWithPath(data)
            .getResourceAsStream("manifest.json")
            .readBytes()
            .toString(Charsets.UTF_8)
            .parsed<FilePluginManifest>()

        return PluginMetadata(manifestData.className, data)
    }
}

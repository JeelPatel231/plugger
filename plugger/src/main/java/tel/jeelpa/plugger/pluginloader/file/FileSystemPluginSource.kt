package tel.jeelpa.plugger.pluginloader.file

import android.os.Build
import android.os.FileObserver
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tel.jeelpa.plugger.PluginSource
import java.io.File


@RequiresApi(Build.VERSION_CODES.Q)
class FileSystemPluginSource(
    private val folder: File,
    private val extension: String,
) : PluginSource<File> {

    constructor(
        filePath: String,
        extension: String,
    ): this(File(filePath), extension)

    private fun loadAllPlugins(): List<File> {
        return folder.listFiles()!!.filter { it.path.endsWith(extension) }
    }

    private val pluginStateFlow = MutableStateFlow(loadAllPlugins())

    private val fsEventsListener = object : FileObserver(folder) {
        override fun onEvent(event: Int, path: String?) {
            pluginStateFlow.value = loadAllPlugins()
        }
    }

    init {
        fsEventsListener.startWatching()
    }

    override fun getSourceFiles(): StateFlow<List<File>> =
        pluginStateFlow.asStateFlow()
}
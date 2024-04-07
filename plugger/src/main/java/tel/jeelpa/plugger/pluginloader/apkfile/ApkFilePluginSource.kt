package tel.jeelpa.plugger.pluginloader.apkfile

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tel.jeelpa.plugger.PluginSource
import tel.jeelpa.plugger.pluginloader.apk.ApkPluginSource
import tel.jeelpa.plugger.pluginloader.file.FileSystemPluginSource
import java.io.File

@RequiresApi(Build.VERSION_CODES.Q)
class ApkFilePluginSource(
    private val packageManager: PackageManager,
    private val folder: File,
    private val extension: String,
) : PluginSource<ApplicationInfo> {

    constructor(
        context: Context,
        filePath: String,
        extension: String,
    ): this(context.packageManager, File(filePath), extension)

    // delegate the loading work to FileSystemLoader,
    // since reading is same, only parsing differs
    // here we parse Manifest.xml instead of manifest.json
    private val filePluginLoader = FileSystemPluginSource(folder, extension)

    override fun getSourceFiles(): Flow<List<ApplicationInfo>> =
        filePluginLoader.getSourceFiles().map { fileList ->
            fileList.map {
                packageManager
                    .getPackageArchiveInfo(it.path, ApkPluginSource.PACKAGE_FLAGS)
                    ?.applicationInfo
                    ?: error("Invalid package, cannot get archive info!")
            }
        }

}

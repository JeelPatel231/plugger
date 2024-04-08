package tel.jeelpa.plugger

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tel.jeelpa.plugger.models.PluginMetadata
import tel.jeelpa.plugger.models.PluginMetadataImpl
import tel.jeelpa.plugger.pluginloader.file.FileSystemPluginSource
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Before
    @After
    fun clearCache() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        appContext.cacheDir.deleteRecursively()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("tel.jeelpa.plugger.test", appContext.packageName)
    }

    @Test
    fun shouldFireEvents(): Unit = runBlocking {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // TODO: throw exceptions in source, parser and loader
        val path = appContext.cacheDir.absolutePath
        val extension = ".mock"
        val mockLoader = object : PluginLoader<PluginMetadata, String> {
            override fun loadPlugin(pluginMetadata: PluginMetadata): String {
                return pluginMetadata.path
            }
        }

        val mockManifestParser = object : ManifestParser<File, PluginMetadata> {
            override fun parseManifest(data: File): PluginMetadata {
                return PluginMetadataImpl("MockClassName", data.path)
            }
        }

        val pluginDir = File(appContext.cacheDir.absolutePath)
        val mockPluginFiles = (1..10).map { File(pluginDir, "$it.mock") }

        assertEquals(true, mockPluginFiles.all { it.createNewFile() })

        val source = FileSystemPluginSource(path, extension)
        val filesystemPluginLoader = PluginRepoImpl(source, mockManifestParser, mockLoader)

        filesystemPluginLoader.getAllPlugins().test {
            assertEquals(mockPluginFiles.map { it.absolutePath }, awaitItem().map { it.getOrThrow().second })

            assertEquals(true, mockPluginFiles.take(3).all { it.delete() })

            assertEquals(awaitItem().map { it.getOrThrow().second }, mockPluginFiles.drop(3).map { it.absolutePath })
        }
    }
}
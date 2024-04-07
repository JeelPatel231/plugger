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
import tel.jeelpa.plugger.pluginloader.file.FilePluginConfig
import tel.jeelpa.plugger.pluginloader.file.FileSystemPluginLoader
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

        val config = FilePluginConfig(appContext.cacheDir.absolutePath, ".mock")
        val mockLoader = object : PluginLoader<String> {
            override fun invoke(pluginMetadata: PluginMetadata): String {
                return pluginMetadata.path
            }
        }

        val mockManifestParser = object : ManifestParser<String> {
            override fun parseManifest(data: String): PluginMetadata {
                return PluginMetadata("MockClassName", data)
            }
        }

        val pluginDir = File(appContext.cacheDir.absolutePath)
        val mockPluginFiles = (1..10).map { File(pluginDir, "$it.mock") }

        assertEquals(true, mockPluginFiles.all { it.createNewFile() })

        val filesystemPluginLoader =
            FileSystemPluginLoader(appContext, config, mockLoader, mockManifestParser)

        filesystemPluginLoader.getAllPlugins().test {
            assertEquals(mockPluginFiles.map { it.absolutePath }, awaitItem().map { it.getOrThrow() })

            assertEquals(true, mockPluginFiles.take(3).all { it.delete() })

            assertEquals(awaitItem().map { it.getOrThrow() }, mockPluginFiles.drop(3).map { it.absolutePath })
        }
    }
}
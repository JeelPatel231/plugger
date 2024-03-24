package tel.jeelpa.plugger

import android.content.pm.ApplicationInfo
import android.os.Bundle
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as mockitoWhen
import tel.jeelpa.plugger.pluginloader.apk.ApkPluginConfiguration
import tel.jeelpa.plugger.pluginloader.apk.ApkPluginManifestParser
import org.junit.Assert.assertEquals
import tel.jeelpa.plugger.models.PluginMetadata

class ManifestParserTests {
    @Test
    fun shouldParseApkManifestCorrectly() {
        val expectedPluginMetadata = PluginMetadata("mockSourceClassTag", "mockPath")

        val pluginConf = ApkPluginConfiguration("mock")
        val apkPluginManifestParser = ApkPluginManifestParser(pluginConf)

        val mockAppInfo = ApplicationInfo()
        val mockMetadata: Bundle = mock()
        mockitoWhen(mockMetadata.getString(pluginConf.metadataSourceClassTag)).thenReturn(
            expectedPluginMetadata.className
        )

        mockAppInfo.sourceDir = expectedPluginMetadata.path
        mockAppInfo.metaData = mockMetadata

        val parsed = apkPluginManifestParser.parseManifest(mockAppInfo)
        assertEquals(parsed, expectedPluginMetadata)
    }

}
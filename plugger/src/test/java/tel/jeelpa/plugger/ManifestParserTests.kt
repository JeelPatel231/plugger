package tel.jeelpa.plugger

import android.content.pm.ApplicationInfo
import android.os.Bundle
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import tel.jeelpa.plugger.models.PluginMetadataImpl
import tel.jeelpa.plugger.pluginloader.apk.ApkPluginManifestParser
import org.mockito.Mockito.`when` as mockitoWhen

class ManifestParserTests {
    @Test
    fun shouldParseApkManifestCorrectly() {
        val expectedPluginMetadata = PluginMetadataImpl("mockSourceClassTag", "mockPath")

        val metadataClassTag = "mock"
        val apkPluginManifestParser = ApkPluginManifestParser(metadataClassTag)

        val mockAppInfo = ApplicationInfo()
        val mockMetadata: Bundle = mock()
        mockitoWhen(mockMetadata.getString(metadataClassTag)).thenReturn(
            expectedPluginMetadata.className
        )

        mockAppInfo.sourceDir = expectedPluginMetadata.path
        mockAppInfo.metaData = mockMetadata

        val parsed = apkPluginManifestParser.parseManifest(mockAppInfo)
        assertEquals(parsed, expectedPluginMetadata)
    }

}
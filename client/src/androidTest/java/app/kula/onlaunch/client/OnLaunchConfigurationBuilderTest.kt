package app.kula.onlaunch.client

import android.app.LocaleManager
import android.os.LocaleList
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class OnLaunchConfigurationBuilderTest {
    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        appContext.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList(Locale.US)
    }

    @Test
    fun buildWithoutPublicKey_throwsIllegalArgumentException() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        assertThrows(IllegalArgumentException::class.java) {
            OnLaunchConfigurationBuilder()
                .getConfig(context = appContext)
        }
    }

    @Test
    fun buildWithPublicKey_returnsDefaultValues() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        OnLaunchConfigurationBuilder()
            .apply {
                publicKey = "publicKey" // Required
                versionName = "0.0" // Default not available in test
            }
            .getConfig(context = appContext).apply {
                assertEquals("publicKey", publicKey)
                assertEquals("https://api.onlaunch.app/api/", baseUrl)
                assertEquals(true, shouldCheckOnInit)
                assertEquals(false, useInAppUpdates)
                assertEquals("app.kula.onlaunch.client.test", packageName)
                assertEquals(
                    "https://play.google.com/store/apps/details?id=app.kula.onlaunch.client.test",
                    appStoreUrl
                )
                assertEquals("en_US", locale)
                assertEquals("en", localeLanguageCode)
                assertEquals("US", localeRegionCode)
            }
    }

    @Test
    fun buildWithAllValuesSet() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        OnLaunchConfigurationBuilder()
            .apply {
                publicKey = "publicKey" // Required
                baseUrl = "https://develop.api.onlaunch.app/api/"
                shouldCheckOnInit = false
                versionName = "0.0"
                useInAppUpdates = true
                packageName = "app.kula.onlaunch.client"
                appStoreUrl = "https://play.google.com/store/apps/details?id=app.kula.editor"
                locale = "en_GB"
                localeLanguageCode = "de"
                localeRegionCode = "DE"
            }
            .getConfig(context = appContext).apply {
                assertEquals("publicKey", publicKey)
                assertEquals("https://develop.api.onlaunch.app/api/", baseUrl)
                assertEquals(false, shouldCheckOnInit)
                assertEquals(true, useInAppUpdates)
                assertEquals("app.kula.onlaunch.client", packageName)
                assertEquals(
                    "https://play.google.com/store/apps/details?id=app.kula.editor",
                    appStoreUrl
                )
                assertEquals("en_GB", locale)
                assertEquals("de", localeLanguageCode)
                assertEquals("DE", localeRegionCode)
            }
    }

    @Test
    fun packageNameDoesNotAffectAppStoreUrl() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        OnLaunchConfigurationBuilder()
            .apply {
                publicKey = "publicKey" // Required
                versionName = "0.0" // Default not available in test
                packageName = "app.kula.onlaunch.client"
            }
            .getConfig(context = appContext).apply {
                assertEquals("app.kula.onlaunch.client", packageName)
                assertEquals(
                    "https://play.google.com/store/apps/details?id=app.kula.onlaunch.client.test",
                    appStoreUrl
                )
            }
    }
}

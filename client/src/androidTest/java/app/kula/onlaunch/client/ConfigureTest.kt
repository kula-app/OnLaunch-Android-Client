package app.kula.onlaunch.client

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*


@RunWith(AndroidJUnit4::class)
class ConfigureTest {
    @Test
    fun configureWithPublicKey_success() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        OnLaunch.init(appContext) {
            apiKey = "apiKey"
        }
    }

    @Test
    fun configureWithoutPublicKey_throwsIllegalArgumentException() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        assertThrows(IllegalArgumentException::class.java) {
            OnLaunch.init(appContext) {}
        }
    }
}

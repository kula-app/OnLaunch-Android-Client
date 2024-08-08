package app.kula.onlaunch.client

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ConfigureTest {
    @Test
    fun configureWithPublicKey_success() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        OnLaunch.init(appContext) {
            publicKey = "publicKey"
            versionName = "0.0" // Default not available in test
            shouldCheckOnInit = false
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

package app.kula.onlaunch.sample

import android.app.Application
import app.kula.onlaunch.client.OnLaunch

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        OnLaunch.init(this) {
            publicKey = "K2UX4fVPFyixVaeLn8Fky_uWhjMr-frADqKqpOCZW2c"
        }
    }
}

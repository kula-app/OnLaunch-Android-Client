package app.kula.onlaunch.sample

import android.app.Application
import app.kula.onlaunch.client.OnLaunch

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        OnLaunch.configure(this) {
            publicKey = ""
        }
    }
}

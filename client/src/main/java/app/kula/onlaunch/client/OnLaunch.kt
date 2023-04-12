package app.kula.onlaunch.client

import android.content.Context
import android.content.Intent
import app.kula.onlaunch.client.data.models.Action
import app.kula.onlaunch.client.data.models.Message
import app.kula.onlaunch.client.ui.OnLaunchActivity

object OnLaunch {
    private var config: OnLaunchConfig? = null

    /**
     * Configure OnLaunch client. publicKey must be set
     * @throws IllegalArgumentException if publicKey is not set
     */
    fun configure(
        context: Context,
        configuration: OnLaunchConfiguration.() -> Unit,
    ) {
        val builder = OnLaunchConfigurationBuilder()
        configuration(builder)
        config = builder.getConfig().also {
            if (it.shouldCheckOnConfigure) {
                check(context)
            }
        }
    }

    /** Checks for new messages */
    fun check(context: Context) {
        val message = Message(
            id = 1,
            title = "Freezing the energy consumption",
            body = "What would George do?\n\nWhen the energy prices are heating up it’s time to cool down and reduce the waste of energy and financial health. While that seems hard, start with the easy wins: for example, when did you defrost your freezer the last time? And by the way, for your normal fridge, 4 degree Celsius are cool enough. Each degree less means 5% more energy.",
            isBlocking = false,
            actions = listOf(
                Action(actionType = Action.Type.DISMISS, title = "Thanks George!"),
            )
        )

        Intent(context, OnLaunchActivity::class.java).apply {
            putExtra(OnLaunchActivity.EXTRA_MESSAGE, message)
            flags += Intent.FLAG_ACTIVITY_NEW_TASK
        }.also {
            context.startActivity(it)
        }
    }
}

private data class OnLaunchConfig(
    val baseUrl: String,
    val publicKey: String,
    val shouldCheckOnConfigure: Boolean,
)

interface OnLaunchConfiguration {
    var baseUrl: String?
    var publicKey: String?
    var shouldCheckOnConfigure: Boolean?
}

private class OnLaunchConfigurationBuilder : OnLaunchConfiguration {
    override var baseUrl: String? = null
    override var publicKey: String? = null
    override var shouldCheckOnConfigure: Boolean? = null

    fun getConfig() = OnLaunchConfig(
        baseUrl = baseUrl ?: "https://onlaunch.kula.app/v1/clients",
        publicKey = publicKey
            ?: throw IllegalArgumentException("Failed to initialize OnLaunch: publicKey not set"),
        shouldCheckOnConfigure = shouldCheckOnConfigure ?: true,
    )
}

package app.kula.onlaunch.client

import android.content.Context
import android.content.Intent
import android.util.Log
import app.kula.onlaunch.client.data.api.OnLaunchApi
import app.kula.onlaunch.client.data.dtos.toMessages
import app.kula.onlaunch.client.data.local.OnLaunchDataStore
import app.kula.onlaunch.client.ui.OnLaunchActivity
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OnLaunch {
    internal const val LOG_TAG = "OnLaunch"

    private var config: OnLaunchConfig? = null
    private lateinit var api: OnLaunchApi
    private lateinit var dataStore: OnLaunchDataStore

    /**
     * Initialize the OnLaunch client. publicKey must be set
     * @throws IllegalArgumentException if publicKey is not set
     */
    fun init(
        context: Context,
        configuration: OnLaunchConfiguration.() -> Unit,
    ) {
        val builder = OnLaunchConfigurationBuilder()
        configuration(builder)
        val config = builder.getConfig()
        this.config = config

        api = Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OnLaunchApi::class.java)

        dataStore = OnLaunchDataStore(context = context)

        if (config.shouldCheckOnInit) {
            check(context)
        }
    }

    /** Checks for new messages */
    fun check(context: Context) = config?.also { config ->
        config.scope.launch {
            Log.d(LOG_TAG, "Checking for messages...")
            val messages = api.getMessages(
                publicKey = config.publicKey,
            ).toMessages()
            val dismissedIds = dataStore.getDismissedMessageIds()

            // Show messages that have not been dismissed yet
            messages.filter { it.id !in dismissedIds }.forEach { message ->
                Intent(context, OnLaunchActivity::class.java).apply {
                    putExtra(OnLaunchActivity.EXTRA_MESSAGE, message)
                    flags += Intent.FLAG_ACTIVITY_NEW_TASK
                }.also {
                    context.startActivity(it)
                }
            }
        }
    }.logNull()

    internal fun markMessageDismissed(messageId: Int) = config?.also { config ->
        config.scope.launch {
            dataStore.addDismissedMessageId(messageId)
        }
    }.logNull()

    private fun OnLaunchConfig?.logNull() {
        if (this == null) {
            Log.w(LOG_TAG, "OnLaunch has not been initialized")
        }
    }
}

private data class OnLaunchConfig(
    val baseUrl: String,
    val publicKey: String,
    val shouldCheckOnInit: Boolean,
    val scope: CoroutineScope,
)

interface OnLaunchConfiguration {
    var baseUrl: String?
    var publicKey: String?
    var shouldCheckOnInit: Boolean?
}

private class OnLaunchConfigurationBuilder : OnLaunchConfiguration {
    override var baseUrl: String? = null
    override var publicKey: String? = null
    override var shouldCheckOnInit: Boolean? = null

    fun getConfig() = OnLaunchConfig(
        baseUrl = baseUrl ?: "https://onlaunch.kula.app/api/",
        publicKey = publicKey
            ?: throw IllegalArgumentException("Failed to initialize OnLaunch: publicKey not set"),
        shouldCheckOnInit = shouldCheckOnInit ?: true,
        scope = (MainScope() + CoroutineExceptionHandler { _, throwable ->
            Log.e(OnLaunch.LOG_TAG, throwable.message, throwable)
        }),
    )
}

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

    private var _config: OnLaunchConfig? = null
    private val config
        get() = _config ?: throw IllegalStateException("OnLaunch has not been initialized")
    private lateinit var api: OnLaunchApi
    private lateinit var dataStore: OnLaunchDataStore

    /**
     * Initialize the OnLaunch client. apiKey must be set
     * @throws IllegalArgumentException if apiKey is not set
     */
    fun init(
        context: Context,
        configuration: OnLaunchConfiguration.() -> Unit,
    ) {
        val builder = OnLaunchConfigurationBuilder()
        configuration(builder)
        _config = builder.getConfig()

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
    fun check(context: Context) = config.scope.launch {
        Log.d(LOG_TAG, "Checking for messages...")
        val messages = api.getMessages(
            apiKey = config.apiKey,
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

    internal fun markMessageDismissed(messageId: Int) = config.scope.launch {
        dataStore.addDismissedMessageId(messageId)
    }
}

private data class OnLaunchConfig(
    val baseUrl: String,
    val apiKey: String,
    val shouldCheckOnInit: Boolean,
    val scope: CoroutineScope,
)

interface OnLaunchConfiguration {
    var baseUrl: String?
    var apiKey: String?
    var shouldCheckOnInit: Boolean?
}

private class OnLaunchConfigurationBuilder : OnLaunchConfiguration {
    override var baseUrl: String? = null
    override var apiKey: String? = null
    override var shouldCheckOnInit: Boolean? = null

    fun getConfig() = OnLaunchConfig(
        baseUrl = baseUrl ?: "https://onlaunch.kula.app/api/",
        apiKey = apiKey
            ?: throw IllegalArgumentException("Failed to initialize OnLaunch: apiKey not set"),
        shouldCheckOnInit = shouldCheckOnInit ?: true,
        scope = (MainScope() + CoroutineExceptionHandler { _, throwable ->
            Log.e(OnLaunch.LOG_TAG, throwable.message, throwable)
        }),
    )
}

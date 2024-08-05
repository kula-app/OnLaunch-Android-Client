package app.kula.onlaunch.client

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import app.kula.onlaunch.client.data.api.OnLaunchApi
import app.kula.onlaunch.client.data.dtos.toMessages
import app.kula.onlaunch.client.data.local.OnLaunchDataStore
import app.kula.onlaunch.client.ui.OnLaunchActivity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
        val config = builder.getConfig(context = context)
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
                packageName = config.packageName,
                versionCode = config.versionCode.toString(),
                versionName = config.versionName,
                platformName = "android",
                platformVersion = android.os.Build.VERSION.SDK_INT.toString(),
                updateAvailable = if (config.useInAppUpdates) checkUpdateAvailable(context = context) else null,
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

    internal fun openAppStore(context: Context) = config?.also { config ->
        Intent(
            Intent.ACTION_VIEW,
            android.net.Uri.parse(config.appStoreUrl)
        ).apply {
            if (config.appStoreUrl.startsWith("https://play.google.com/store/apps/details?id=")) {
                setPackage("com.android.vending")
            }
            try {
                context.startActivity(this)
            } catch (e: ActivityNotFoundException) {
                Log.e(LOG_TAG, "Failed to open app store", e)
            }
        }
    }.logNull()

    private fun OnLaunchConfig?.logNull() {
        if (this == null) {
            Log.w(LOG_TAG, "OnLaunch has not been initialized")
        }
    }

    private suspend fun checkUpdateAvailable(context: Context): Boolean? {
        val appUpdateInfoTask = AppUpdateManagerFactory.create(context).appUpdateInfo
        return suspendCoroutine { continuation ->
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                continuation.resume(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
            }
            appUpdateInfoTask.addOnCanceledListener {
                continuation.resume(null)
            }
            appUpdateInfoTask.addOnCanceledListener {
                continuation.resume(null)
            }
            appUpdateInfoTask.addOnFailureListener { e ->
                Log.e(LOG_TAG, "Checking for updates failed", e)
                continuation.resume(null)
            }
        }
    }
}

private data class OnLaunchConfig(
    val baseUrl: String,
    val publicKey: String,
    val shouldCheckOnInit: Boolean,
    val scope: CoroutineScope,
    val packageName: String,
    val versionCode: Long,
    val versionName: String,
    val useInAppUpdates: Boolean,
    val appStoreUrl: String,
)

interface OnLaunchConfiguration {
    var baseUrl: String?
    var publicKey: String?

    /**
     *  If set to true, OnLaunch will check for messages on initialization.
     *
     * Defaults to true
     */
    var shouldCheckOnInit: Boolean?
    var packageName: String?
    var versionCode: Long?
    var versionName: String?
    var appStoreUrl: String?

    /**
     * Set to true to use Google Play In-App Updates to check for available updates.
     * When using Google Play In-App Updates you have to accept the Google Play Terms of Service.
     *
     * Defaults to false.
     * @see https://developer.android.com/guide/playcore/in-app-updates
     */
    var useInAppUpdates: Boolean?
}

private class OnLaunchConfigurationBuilder : OnLaunchConfiguration {
    override var baseUrl: String? = null
    override var publicKey: String? = null
    override var shouldCheckOnInit: Boolean? = null
    override var packageName: String? = null
    override var versionCode: Long? = null
    override var versionName: String? = null
    override var useInAppUpdates: Boolean? = null
    override var appStoreUrl: String? = null

    fun getConfig(context: Context) = OnLaunchConfig(
        baseUrl = baseUrl ?: "https://onlaunch.kula.app/api/",
        publicKey = publicKey
            ?: throw IllegalArgumentException("Failed to initialize OnLaunch: publicKey not set"),
        shouldCheckOnInit = shouldCheckOnInit ?: true,
        scope = (MainScope() + CoroutineExceptionHandler { _, throwable ->
            Log.e(OnLaunch.LOG_TAG, throwable.message, throwable)
        }),
        versionCode = versionCode ?: context.packageManager.getPackageInfo(
            context.packageName,
            0
        ).versionCode.toLong(),
        versionName = versionName ?: context.packageManager.getPackageInfo(
            context.packageName,
            0
        ).versionName,
        packageName = packageName ?: context.packageName,
        useInAppUpdates = useInAppUpdates ?: false,
        appStoreUrl = appStoreUrl
            ?: "https://play.google.com/store/apps/details?id=${context.packageName}",
    )
}

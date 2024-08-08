package app.kula.onlaunch.client

import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.plus
import java.util.Locale

internal class OnLaunchConfigurationBuilder : OnLaunchConfiguration {
    override var baseUrl: String? = null
    override var publicKey: String? = null
    override var shouldCheckOnInit: Boolean? = null
    override var packageName: String? = null
    override var versionCode: Long? = null
    override var versionName: String? = null
    override var useInAppUpdates: Boolean? = null
    override var appStoreUrl: String? = null
    override var locale: String? = null
    override var localeLanguageCode: String? = null
    override var localeRegionCode: String? = null

    internal fun getConfig(context: Context) = OnLaunchConfig(
        baseUrl = baseUrl ?: "https://onlaunch.kula.app/api/",
        publicKey = publicKey
            ?: throw IllegalArgumentException("Failed to initialize OnLaunch: publicKey not set"),
        shouldCheckOnInit = shouldCheckOnInit ?: true,
        scope = (MainScope() + CoroutineExceptionHandler { _, throwable ->
            Log.e(OnLaunch.LOG_TAG, throwable.message, throwable)
        }),
        versionCode = versionCode ?: getVersionCode(context),
        versionName = versionName ?: context.packageManager.getPackageInfo(
            context.packageName,
            0
        ).versionName,
        packageName = packageName ?: context.packageName,
        useInAppUpdates = useInAppUpdates ?: false,
        appStoreUrl = appStoreUrl
            ?: "https://play.google.com/store/apps/details?id=${context.packageName}",
        locale = locale ?: getLocale(context).toString(),
        localeLanguageCode = localeLanguageCode ?: getLocale(context).language,
        localeRegionCode = localeRegionCode ?: getLocale(context).country
    )

    private fun getVersionCode(context: Context): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.packageManager.getPackageInfo(
                context.packageName,
                0
            ).longVersionCode
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(
                context.packageName,
                0
            ).versionCode.toLong()
        }
    }

    private fun getLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
    }
}

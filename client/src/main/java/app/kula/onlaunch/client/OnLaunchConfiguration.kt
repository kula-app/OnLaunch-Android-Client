package app.kula.onlaunch.client

import android.content.Context

interface OnLaunchConfiguration {
    /**
     * Base URL where the OnLaunch API is hosted at. Change this to point to your self-hosted instance of the OnLaunch server.
     *
     * Defaults to `https://onlaunch.kula.app/api/`
     */
    var baseUrl: String?

    /** Public key used to authenticate with the API */
    var publicKey: String?

    /**
     * If set to true, OnLaunch will check for messages on initialization.
     *
     * Defaults to `true`
     */
    var shouldCheckOnInit: Boolean?

    /** The package name of the app. Used by server-side rule evaluation. */
    var packageName: String?

    /** The version code of the app. Used by server-side rule evaluation. */
    var versionCode: Long?

    /** The version name of the app. Used by server-side rule evaluation. */
    var versionName: String?

    /**
     * URL to the app store where the app can be updated. Used to open the app store.
     * The package name in the default value is NOT the `packageName` parameter, but the package name provided by the [Context].
     *
     * Defaults to `https://play.google.com/store/apps/details?id=<PACKAGE_NAME>`
     */
    var appStoreUrl: String?

    /**
     * Set to true to use Google Play In-App Updates to check for available updates.
     * When using Google Play In-App Updates you have to accept the Google Play Terms of Service.
     *
     * Defaults to `false`
     * @see https://developer.android.com/guide/playcore/in-app-updates
     */
    var useInAppUpdates: Boolean?

    /** The locale of the app. Used by server-side rule evaluation. */
    var locale: String?

    /** The language code of the locale. Used by server-side rule evaluation. */
    var localeLanguageCode: String?

    /** The region code of the locale. Used by server-side rule evaluation. */
    var localeRegionCode: String?
}

# OnLaunch Android Client

<p align="center">
  <a href="https://github.com/kula-app/OnLaunch-Android-Client/releases">
    <img src="https://img.shields.io/github/release/kula-app/onlaunch-android-client.svg"/>
  </a>
  <a href="https://github.com/kula-app/OnLaunch-Android-Client/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/kula-app/OnLaunch-Android-Client.svg"/>
  </a>
</p>

<p align="center">
    <sub>Created and maintained by <a href="https://kula.app">kula.app</a> and all the amazing <a href="https://github.com/kula-app/OnLaunch-Android-Client/graphs/contributors">contributors</a>.</sub>
</p>

[OnLaunch](https://github.com/kula-app/OnLaunch) is a service allowing app developers to notify app
users about updates, warnings and maintenance
Our open-source framework provides an easy-to-integrate client to communicate with the backend and
display the user interface.

<p align="center">
  <img src="/docs/android_onlaunch_example.png" alt="OnLaunch Android" width="300"/>
</p>

## Features

- Display customizable messages to your app users
- Set your app into maintenance mode with blocking messages
- Easy to integrate

## Installation

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("app.kula:onlaunch-android-client:0.0.6")
}
```

## Usage

1. Initialize `OnLaunch`:

```kotlin
OnLaunch.init(this) {
    publicKey = "<YOUR PUBLIC APP KEY>"
}
```

2. Optionally manually check for new messages (e.g. in `onResume`):

```kotlin
override fun onResume() {
    super.onResume()
    OnLaunch.check(this)
}
```

### Options

The OnLaunch Android client provides a couple of configuration options:

| Name                 | Description                                                                                                                                                                                                                        | Default                                                                      |
| -------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------- |
| `publicKey`          | Public key used to authenticate with the API.                                                                                                                                                                                      |                                                                              |
| `baseUrl`            | Base URL where the OnLaunch API is hosted at. Change this to point to your self-hosted instance of the OnLaunch server.                                                                                                            | `https://api.onlaunch.app/api/`                                              |
| `shouldCheckOnInit`  | Flag indicating if the client should check for new messages immediately after it has been initialized.                                                                                                                             | `true`                                                                       |
| `useInAppUpdates`    | Set to `true` to use Google Play In-App Updates to check for available updates. When using Google Play In-App Updates you accept the Google Play Terms of Service. See https://developer.android.com/guide/playcore/in-app-updates | `false`                                                                      |
| `appStoreUrl`        | URL to the app store where the app can be updated. Used to open the app store. The package name in the default value is NOT the `packageName` parameter, but the package name provided by the `Context`.                           | `https://play.google.com/store/apps/details?id=<PACKAGE_NAME>`               |
| `packageName`        | The package name of the app. Used by server-side rule evaluation.                                                                                                                                                                  | Package name defined in the context                                          |
| `versionCode`        | The version code of the app. Used by server-side rule evaluation.                                                                                                                                                                  | Version code defined in the package manager context                          |
| `versionName`        | The version name of the app. Used by server-side rule evaluation.                                                                                                                                                                  | Version name defined in the package manager context                          |
| `locale`             | The locale of the app. Used by server-side rule evaluation.                                                                                                                                                                        | Locale defined in `context.resources.configuration.locale`, i.e. `en_US`     |
| `localeLanguageCode` | The language code of the locale. Used by server-side rule evaluation.                                                                                                                                                              | Language code defined in `context.resources.configuration.locale`, i.e. `en` |
| `localeRegionCode`   | The region code of the locale. Used by server-side rule evaluation.                                                                                                                                                                | Region code defined in `context.resources.configuration.locale`, i.e. `US`   |

# Contributing Guide

Please see
our [Contributing Guide](https://github.com/kula-app/OnLaunch-Android-Client/blob/main/CONTRIBUTING.md)
.

## License

Distributed under
the [MIT License](https://github.com/kula-app/OnLaunch-Android-Client/blob/main/LICENSE)

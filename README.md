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
    <sub>Created and maintained by <a href="https://kula.app">kula.app</a> and all the amazing <a href="https://github.com/kula-app/OnLaunch-iOS-Client/graphs/contributors">contributors</a>.</sub>
</p>

[OnLaunch](https://github.com/kula-app/OnLaunch) is a service allowing app developers to notify app
users about updates, warnings and maintenance
Our open-source framework provides an easy-to-integrate client to communicate with the backend and
display the user interface.

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
    implementation("app.kula:onlaunch-android-client:0.0.5")
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

| Name                  | Description                                                                                                             | Default                           |
|-----------------------|-------------------------------------------------------------------------------------------------------------------------|-----------------------------------|
| `publicKey`           | Public key used to authenticate with the API                                                                            |                                   |
| `baseUrl`             | Base URL where the OnLaunch API is hosted at. Change this to point to your self-hosted instance of the OnLaunch server. | `https://onlaunch.kula.app/api/`  |
| `shouldCheckOnInit`   | Flag indicating if the client should check for new messages immediately after it has been initialized.                  | `true`                            |

# Contributing Guide

Please see
our [Contributing Guide](https://github.com/kula-app/OnLaunch-Android-Client/blob/main/CONTRIBUTING.md)
.

## License

Distributed under
the [MIT License](https://github.com/kula-app/OnLaunch-Android-Client/blob/main/LICENSE)

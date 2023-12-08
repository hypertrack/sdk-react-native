# React Native HyperTrack SDK

[![GitHub](https://img.shields.io/github/license/hypertrack/sdk-react-native?color=orange)](./LICENSE)
[![npm](https://img.shields.io/npm/v/hypertrack-sdk-react-native.svg)](https://www.npmjs.com/package/hypertrack-sdk-react-native)
[![iOS SDK](https://img.shields.io/badge/iOS%20SDK-5.0.8-brightgreen.svg)](https://github.com/hypertrack/sdk-ios)
[![Android SDK](https://img.shields.io/badge/Android%20SDK-7.0.10-brightgreen.svg)](https://github.com/hypertrack/sdk-android)

[HyperTrack](https://www.hypertrack.com) lets you add live location tracking to your mobile app. Live location is made available along with ongoing activity, tracking controls and tracking outage with reasons.

React Native HyperTrack SDK is a wrapper around native iOS and Android SDKs that allows to integrate HyperTrack into React Native apps.

For information about how to get started with React Native HyperTrack SDK, please check this [Guide](https://www.hypertrack.com/docs/install-sdk-react-native).

## Installation

```
yarn add hypertrack-sdk-react-native
yarn add hypertrack-sdk-react-native-plugin-android-location-services-google
yarn add hypertrack-sdk-react-native-plugin-android-push-service-firebase
```

[Learn more about plugins](#plugins)

## Sample code

[Quickstart React Native app](https://github.com/hypertrack/quickstart-react-native)

## Wrapper API Documentation

[Wrapper API Documentation](https://hypertrack.github.io/sdk-react-native/)

## Requirements

### Android

- Gradle version 6.7.1+
- compileSdkVersion 31+
- minSdkVersion 19+

### iOS

- iOS version 12.4+

## Plugins

**The HyperTrack SDK Android version and the plugins versions have to be the same.**

HyperTrack SDK Android uses the [Plugin architecture](https://github.com/hypertrack/sdk-android/blob/master/PLUGINS.md) to be able to adapt to different dependencies and add configurable logic.

Adding the plugin to React Native project will affect only the Android app. If you want to use only the iOS, you don't need to add any of them.

The Android setup for HyperTrack SDK have to include at least one plugin of each of those types:

    - Location Services
    - Push Service

Check the [plugins list](https://github.com/hypertrack/sdk-android/blob/master/PLUGINS.md#available-plugins) to get the type and description for each of them.

Each HyperTrack SDK Android plugin has the corresponding React Native package.

| React Native Package Name                                                  | HyperTrack SDK Android Plugin Maven Artifact ID |
| -------------------------------------------------------------------------- | ----------------------------------------------- |
| hypertrack-sdk-react-native-plugin-android-location-services-google        | location-services-google                        |
| hypertrack-sdk-react-native-plugin-android-location-services-google-19-0-1 | location-services-google-19-0-1                 |
| hypertrack-sdk-react-native-plugin-android-push-service-firebase           | push-service-firebase                           |

## Contributing

If you want to contribute check [CONTRIBUTING.md](CONTRIBUTING.md)

# React Native HyperTrack SDK

![GitHub](https://img.shields.io/github/license/hypertrack/sdk-react-native.svg) ![npm](https://img.shields.io/npm/v/hypertrack-sdk-react-native.svg) ![iOS SDK](https://img.shields.io/badge/iOS%20SDK-4.13.0-brightgreen.svg) ![Android SDK](https://img.shields.io/badge/Android%20SDK-6.3.0-brightgreen.svg)

[HyperTrack](https://www.hypertrack.com) lets you add live location tracking to your mobile app. Live location is made available along with ongoing activity, tracking controls and tracking outage with reasons.

React Native HyperTrack SDK is a wrapper around native iOS and Android SDKs that allows to integrate them into React Native apps.

## Installation

For information about how to get started with React Native SDK, please visit this [HyperTrack Guide](https://www.hypertrack.com/docs/install-sdk-react-native).

## Documentation

[https://hypertrack.github.io/sdk-react-native/](https://hypertrack.github.io/sdk-react-native/)

## FAQ

link to FAQ

## Support

Join our [Slack community](https://join.slack.com/t/hypertracksupport/shared_invite/enQtNDA0MDYxMzY1MDMxLTdmNDQ1ZDA1MTQxOTU2NTgwZTNiMzUyZDk0OThlMmJkNmE0ZGI2NGY2ZGRhYjY0Yzc0NTJlZWY2ZmE5ZTA2NjI) for instant responses. You can also email us at help@hypertrack.com.

#### How to update the HyperTrack SDK version?

1. Update SDK constant

   - android
     - android/build.gradle
       -
   - ios
     - hypertrack-sdk-react-native.podspec
       - s.dependency 'HyperTrack/Objective-C', '**version**'

2. Increment wrapper version
   - package.json
     - version
3. Update changelog
4. Update badge in README.md
5. Create a version tag
6. Commit and push
7. Create a release
   - Release title should be the current version tag
8. npm publish

### How to change build config

Change Android build config in `android/gradle.properties`

### Old React Native versions specifics:

#### Android

- Gradle version should be 6.7.1 or higher
- compileSdkVersion should be 31 or higher

#### iOS

- Platform version should be 12.4 or higher

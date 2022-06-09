# React Native HyperTrack SDK

![GitHub](https://img.shields.io/github/license/hypertrack/sdk-react-native.svg)
![npm](https://img.shields.io/npm/v/hypertrack-sdk-react-native.svg)
[![iOS SDK](https://img.shields.io/badge/iOS%20SDK-4.12.0-brightgreen.svg)](https://cocoapods.org/pods/HyperTrack)
![Android SDK](https://img.shields.io/badge/Android%20SDK-6.1.2-brightgreen.svg)

[HyperTrack](https://www.hypertrack.com) lets you add live location tracking to your mobile app. Live location is made available along with ongoing activity, tracking controls and tracking outage with reasons.

React Native HyperTrack SDK is a wrapper around native iOS and Android SDKs that allows to integrate them into React Native apps.

For information about how to get started with React Native SDK, please visit this [HyperTrack Guide](https://www.hypertrack.com/docs/install-sdk-react-native).

## How to update the HyperTrack SDK version?


1. Update SDK constant

    - android
        - android/build.gradle
             - DEFAULT_HYPERTRACK_SDK_VERSION 
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
9. npm publish

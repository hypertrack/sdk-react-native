# React Native Quickstart for HyperTrack SDKs

![GitHub](https://img.shields.io/github/license/hypertrack/sdk-react-native.svg) ![npm](https://img.shields.io/npm/v/hypertrack-sdk-react-native.svg) ![iOS SDK](https://img.shields.io/badge/iOS%20SDK-4.12.3-brightgreen.svg) ![Android SDK](https://img.shields.io/badge/Android%20SDK-6.1.2-brightgreen.svg)

[HyperTrack](https://www.hypertrack.com/) lets you add live location tracking to your mobile app. Live location is made available along with ongoing activity, tracking controls and tracking outage with reasons. This repo contains an example React Native app that has everything you need to get started in minutes.

## Create HyperTrack Account

[Sign up](https://dashboard.hypertrack.com/signup) for HyperTrack and get your publishable key from the [Setup page](https://dashboard.hypertrack.com/setup).

## Clone Quickstart app

```sh
git clone https://github.com/hypertrack/quickstart-react-native
```

### Install SDKs Dependencies
**General Dependencies**
- `yarn` or `npm install`

**iOS dependencies**

Quickstart app uses [CocoaPods](https://cocoapods.org/) dependency manager to install the latest version of the iOS SDK. Using the latest version of CocoaPods is advised.

If you don't have CocoaPods, [install it first](https://guides.cocoapods.org/using/getting-started.html#installation).
```sh
cd ios
pod install
```

Insert your HyperTrack publishable key to `HyperTrackPublishableKey` string in `ios/HyperTrackSdkExample/Info.plist`

### Set up silent push notifications (needs to be reviewed)

Set up silent push notifications to manage on-device tracking using HyperTrack cloud APIs from your server.

> If you prefer to use your own messaging service to manage server-to-device communication, use the `sync()` method.

Log into the HyperTrack dashboard, and open the [setup page](https://dashboard.hypertrack.com/setup) and scroll to "Server to Device communication" section.

#### [](https://github.com/hypertrack/quickstart-react-native#android)Android

Enter your "Server key", which you can obtain by going to your [Firebase Console](https://console.firebase.google.com/), navigate to your project, project settings, Cloud Messaging and copying it from "Project credentials" section.

#### [](https://github.com/hypertrack/quickstart-react-native#ios)iOS

Upload your Auth Key (file in the format `AuthKey_KEYID.p8` obtained/created from Apple Developer console > Certificates, Identifiers & Profiles > Keys) and fill in your Team ID (Can be seen in Account > Membership).

### [](https://github.com/hypertrack/quickstart-react-native#run-the-app)Run the app

To run the iOS version open the app's workspace file (`/ios/Quickstart.xcworkspace`) with Xcode. Select your device (SDK requires real device, and won't work using simulator) and hit Run.

To run the Android version execute `react-native run-android` in the repo's root directory.

Enable location and activity permissions (choose "Always Allow" for location).

> HyperTrack creates a unique internal device identifier that's used as mandatory key for all HyperTrack API calls. Please be sure to get the `device_id` from the app or the logs. The app calls `getDeviceID()` to retrieve it.

You may also set device name and metadata using the [Devices API](https://www.hypertrack.com/docs/references/#references-apis-devices-set-device-name-and-metadata)

### Run the app

To run the iOS version open the app's workspace file (`/ios/Quickstart.xcworkspace`) with Xcode. Select your device (SDK requires real device, and won't work using simulator) and hit Run.

To run the Android version execute `react-native run-android` in the repo's root directory.

Enable location and activity permissions (choose "Always Allow" for location).

> HyperTrack creates a unique internal device identifier that's used as mandatory key for all HyperTrack API calls. Please be sure to get the `device_id` from the app or the logs. The app calls `getDeviceID()` to retrieve it.

You may also set device name and metadata using the [Devices API](https://www.hypertrack.com/docs/references/#references-apis-devices-set-device-name-and-metadata)
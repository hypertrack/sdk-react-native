# Install SDK on React Native

![GitHub](https://img.shields.io/github/license/hypertrack/sdk-react-native.svg) ![npm](https://img.shields.io/npm/v/hypertrack-sdk-react-native.svg)

[HyperTrack](https://www.hypertrack.com/) lets you add live location tracking to your mobile app. Live location is made available along with ongoing activity, tracking controls and tracking outage with reasons.

React Native HyperTrack SDK is a wrapper around native iOS and Android SDKs that allows to integrate them into React Native apps.

SDK provides access to functionality that collects location, activity and device health data. The module exposes methods to start and stop tracking, and interact with the HyperTrack APIs.

Functionality is provided through one single npm package so you can use it for both platforms without downloading any extra packages.

## Step 1. Requirements
-   [HyperTrack Account](https://hypertrack.com/)
-   Your HyperTrack publishable key, available in [Setup > Publishable Key](https://dashboard.hypertrack.com/setup).

### iOS Requirements
-   An iOS 11+ or iPadOS device (iPhone, iPad, iPod Touch) to test on. SDK requires real device, and won't work using simulator.
-   A Mac with Xcode 12+.
-   [An iOS Push Certificate](https://developer.apple.com/documentation/usernotifications/registering_your_app_with_apns).

### Android Requirements
-   An Android 5.0.0+ device or emulator with "Google Play Store (Services)" installed.
-   [A Google/Firebase Server API Key](https://firebase.google.com/docs/android/setup).
-   Project using [AndroidX](https://developer.android.com/jetpack/androidx/migrate).


> ### 📘 Running Example Project
> 
> For your convenience, we created an example project, based on React Native 0.63.4.  
> You can run this project to test configurations, debug, and build upon it.
>
> -   `git clone https://github.com/hypertrack/sdk-react-native`
> -   `cd sdk-react-native && cd example`
> -   `yarn` or `npm install`
> -   Running the Android example app: `react-native run-android`
> -   Running the iOS example app:
> 	-   Open the `/ios/HyperTrackSdkExample.xcworkspace` workspace file in Xcode
>     -   Change the **Signing Team** and **Bundle Identifier** for both the HyperTrackSdkExample target 
>     - Insert your HyperTrack publishable key to `HyperTrackPublishableKey` string in `ios/HyperTrackSdkExample/Info.plist`
>     -   Select your device (SDK requires real device, and won't work using simulator) and hit Run

> ### 🚧 Expo Setup
> TBD


### Step 2: Install the HyperTrack package to your app[#](https://hypertrack.com/docs/install-sdk-react-native#step-2-install-javascript-packages "Direct link to heading")
2.1 Install the SDK using Yarn or NPM
-   Yarn: `yarn add hypertrack-sdk-react-native`
-   NPM `npm install --save hypertrack-sdk-react-native`

2.2 Link HyperTrack **(for RN versions < 0.60)**

**Skip if using React Native version of 0.60 or greater.** [Autolinking is now done automatically](https://reactnative.dev/blog/2019/07/03/version-60#native-modules-are-now-autolinked) so skip to step 3.

React Native: `react-native link hypertrack-sdk-react-native`

>##### ℹ️ NOTE
>We constantly work on making our SDKs better, so make sure you have the latest version of it. You might take a look of its changelog [here](https://github.com/hypertrack/sdk-react-native/blob/master/CHANGELOG.md).

### Step 3: Configure projects[#](https://hypertrack.com/docs/install-sdk-react-native#step-3-configure-projects "Direct link to heading")
#### 3.1 Android[#](https://hypertrack.com/docs/install-sdk-react-native#android "Direct link to heading")
Assuming you have latest [Android Studio](http://developer.android.com/sdk/index.html) installed, open the project with Android Studio.

Go to `app/build.gradle` and change minimum sdk version:


```sh
minSdkVersion = 21
```

Add in repositories section:
```sh
maven {
	name 'hypertrack'
	url 'https://s3-us-west-2.amazonaws.com/m2.hypertrack.com/'
}
```

#### 3.2 iOS[#](https://hypertrack.com/docs/install-sdk-react-native#ios "Direct link to heading")

** Add HyperTrack iOS SDK to your Podfile**[#](https://hypertrack.com/docs/install-sdk-react-native#add-hypertrack-ios-sdk-to-your-podfile "Direct link to heading")

The native iOS SDK is distributed using [CocoaPods](https://cocoapods.org/) dependency manager.

If you don't have CocoaPods, [install it first](https://guides.cocoapods.org/using/getting-started.html#installation). Using the latest version is advised.

HyperTrack iOS SDK supports iOS 11 and above, that's why `platform :ios, '11.0'` should be included explicitly in `Project Root/ios/Podfile`.

##### 3.2.1 Run `cd ios && pod install`.

##### 3.2.2 Add Required Capabilities:

**Enable background location updates**[#](https://hypertrack.com/docs/install-sdk-react-native/#enable-background-location-updates "Direct link to heading")

Enable Background Modes in your project target's Capabilities tab. Choose "Location updates".

![Capabilities tab in Xcode](https://hypertrack.com/docs/img/Background_Modes.png)

**Add purpose strings**[#](https://hypertrack.com/docs/install-sdk-react-native/#add-purpose-strings "Direct link to heading")

Set the following purpose strings in the `Project Root/ios/Info.plist` file:

```sh
<key>NSLocationAlwaysAndWhenInUseUsageDescription</key>

<string>To let your friends and family track you live, you need to allow HyperTrack Live to access this device's location</string>

<key>NSLocationAlwaysUsageDescription</key>

<string>To let your friends and family track you live, you need to allow HyperTrack Live to access this device's location</string>

<key>NSLocationWhenInUseUsageDescription</key>

<string>To let your friends and family track you live, you need to allow HyperTrack Live to access this device's location</string>

<key>NSMotionUsageDescription</key>

<string>To track your movement accurately, HyperTrack Live needs to access motion sensors</string>

<key>UIBackgroundModes</key>

<array>

	<string>location</string>
	
	<string>remote-notification</string>

</array>
```

Be advised, purpose strings are mandatory, and the app crashes without them.

##### Step 3.3: Enable remote notifications[#](https://hypertrack.com/docs/install-sdk-react-native/#step-4-enable-remote-notifications "Direct link to heading")

The SDK has a bi-directional communication model with the server. This enables the SDK to run on a variable frequency model, which balances the fine trade-off between low latency tracking and battery efficiency, and improves robustness. This also enables HyperTrack Trips to start and stop tracking automatically when trip starts and ends. For this purpose, the iOS SDK uses APNs silent remote notifications and Android SDK uses FCM silent notifications.

**3.3.1 Android**[#](https://hypertrack.com/docs/install-sdk-react-native/#android-1 "Direct link to heading")

This guide assumes you have configured FCM in your application. If you haven't, read the [Firebase guide](https://firebase.google.com/docs/android/setup).

**Configure FCM key on the Dashboard**[#](https://hypertrack.com/docs/install-sdk-react-native/#configure-fcm-key-on-the-dashboard "Direct link to heading")

Log into the HyperTrack dashboard, and open the [setup page](https://dashboard.hypertrack.com/setup). Enter your FCM Key.

This key will only be used to send remote push notifications to your apps.

**3.3.2 iOS**[#](https://hypertrack.com/docs/install-sdk-react-native/#ios-1 "Direct link to heading")

This guide assumes you have configured APNs in your application. If you haven't, read the [iOS documentation on APNs](https://developer.apple.com/documentation/usernotifications/registering_your_app_with_apns).

**Configure APNs on the dashboard**[#](https://hypertrack.com/docs/install-sdk-react-native/#configure-apns-on-the-dashboard "Direct link to heading")

Log into the HyperTrack dashboard, and open the [setup page](https://dashboard.hypertrack.com/setup). Upload your Auth Key (file in the format `AuthKey_KEYID.p8`) and fill in your Team ID.

This key will only be used to send remote push notifications to your apps.

**Enable remote notifications in the app**[#](https://hypertrack.com/docs/install-sdk-react-native/#enable-remote-notifications-in-the-app "Direct link to heading")

In the app capabilities, ensure that **remote notifications** inside background modes is enabled.

![Remote Notifications in Xcode](https://hypertrack.com/docs/img/Remote_Notifications.png)

In the same tab, ensure that **push notifications** is enabled.

![Push Notifications in Xcode](https://hypertrack.com/docs/img/Push_Notifications.png)

**Registering and receiving notifications**[#](https://hypertrack.com/docs/install-sdk-react-native/#registering-and-receiving-notifications "Direct link to heading")

The following changes inside AppDelegate will register the SDK for push notifications and route HyperTrack notifications to the SDK. Open your /ios/{projectName}/AppDelegate.m file.

At the top of the file, import the HyperTrack SDK:

```sh
@import HyperTrack;
```

**Register for notifications**[#](https://hypertrack.com/docs/install-sdk-react-native/#register-for-notifications "Direct link to heading")

Inside `didFinishLaunchingWithOptions`, use the SDK method to register for notifications.

```sh
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
	[HTSDK registerForRemoteNotifications];
	return YES;
}
```

**Register device token**[#](https://hypertrack.com/docs/install-sdk-react-native/#register-device-token "Direct link to heading")

Inside and `didRegisterForRemoteNotificationsWithDeviceToken` and `didFailToRegisterForRemoteNotificationsWithError` methods, add the relevant lines so that HyperTrack can register the device token.

```sh
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
	[HTSDK didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
	[HTSDK didFailToRegisterForRemoteNotificationsWithError:error];
}
```

**Receive notifications**[#](https://hypertrack.com/docs/install-sdk-react-native/#receive-notifications "Direct link to heading")

Inside the `didReceiveRemoteNotification` method, add the HyperTrack receiver. This method parses only the notifications that sent from HyperTrack.

```sh
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
	[HTSDK didReceiveRemoteNotification:userInfo fetchCompletionHandler:completionHandler];
}
```

If you want to make sure to only pass HyperTrack notifications to the SDK, you can use the "hypertrack" key:
```sh
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
	if (userInfo[@"hypertrack"] != nil) {
		// This is HyperTrack's notification
		[HTSDK didReceiveRemoteNotification:userInfo fetchCompletionHandler:completionHandler];
	} else {
		// Handle your server's notification here
	}
}
```

### Step 5: Usage[#](https://hypertrack.com/docs/install-sdk-react-native/#step-5-usage "Direct link to heading")

TBD


#### You are all set[#](https://hypertrack.com/docs/install-sdk-react-native#you-are-all-set "Direct link to heading")

You can now run the app and start using HyperTrack. You can see your devices on the [dashboard](https://hypertrack.com/docs/install-sdk-react-native#dashboard).
# React Native module for HyperTrack SDKs

![GitHub](https://img.shields.io/github/license/hypertrack/sdk-react-native.svg)
![npm](https://img.shields.io/npm/v/hypertrack-sdk-react-native.svg)

[HyperTrack](https://www.hypertrack.com) lets you add live location tracking to your mobile app. Live location is made available along with ongoing activity, tracking controls and tracking outage with reasons. This repo contains an example React Native app that has everything you need to get started in minutes.

* [Publishable Key](#publishable-key)–Sign up and get your keys
* [Integrate the React Native module](#integrate-the-react-native-module)–Integrate the React Native module into your app
* [Dashboard](#dashboard)–See live location of all your devices on your HyperTrack dashboard
* [FAQs](#frequently-asked-questions)–Frequently asked questions
* [Support](#support)–Support

## Publishable Key

We use Publishable Key to idetify your devices. To get one:
1. Go to the [Signup page](https://dashboard.hypertrack.com/signup). Enter your email address and password.
2. Open the verification link sent to your email.
3. Open the [Setup page](https://dashboard.hypertrack.com/setup), where you can copy your Publishable Key.

![Signup flow](Images/Signup_flow.png)

## Integrate the React Native module

This guide will help you install the React Native module in your application. The module is built on top of native iOS and Android SDKs, which collect location, activity and device health data. The module exposes methods to start and stop tracking, and interact with the HyperTrack APIs.

In case you are not using React Native, you can refer to the [iOS](https://github.com/hypertrack/quickstart-ios) or [Android](https://github.com/hypertrack/quickstart-android) versions.

If you want to quickly try the module in a test app, you can use the [React Native Quickstart app](https://github.com/hypertrack/quickstart-react-native).

1. [Install the module](#step-1-install-the-module)
2. [Setup native dependencies](#step-2-setup-native-dependencies)
3. [Import the module](#step-3-import-the-module)
4. [Configure the Publishable Key](#step-4-configure-the-publishable-key)
5. [Enable native capabilities on iOS](#step-5-enable-native-capabilities-on-ios)

### Step 1: Install the module

In your project directory, install the module from npm, and then link it.

```bash
npm install hypertrack-sdk-react-native --save
react-native link hypertrack-sdk-react-native
```

### Step 2: Setup native dependencies

#### iOS

##### Add HyperTrack iOS SDK to your Podfile

The native iOS SDK is distributed using [CocoaPods](https://cocoapods.org) dependency manager.

If you don't have CocoaPods, [install it first](https://guides.cocoapods.org/using/getting-started.html#installation). Using the latest version is advised.

In your project's `ios` directory, create a Podfile (if you don't have one).

```bash
cd ios
pod init
```

Edit the Podfile to include `HyperTrack` as a dependency for your project by adding `pod 'HyperTrack'` line in your target. HyperTrack iOS SDK supports iOS 10 and above, that's why `platform :ios, '10.0'` is included explicitly. And lastly, add the `post_install` script to keep dependencies with the correct Swift version.

```bash
platform :ios, '10.0'

target 'AppName' do
  use_frameworks!
  pod 'HyperTrack'
end

post_install do |installer|
  installer.pods_project.targets.each do |target|
    if ['GRDB.swift', 'CocoaLumberjack'].include? target.name
      target.build_configurations.each do |config|
        config.build_settings['SWIFT_VERSION'] = '4.2'
      end
    end
  end
end
```

Check the Swift version in build settings. Open your `.xcodeproj` project file with Xcode. Select the projects Xcode project file in the navigator, go to your app's target (Should be with the same name as a project name) > Build Settings and search for `SWIFT_VERSION` flag. If there isn't one, create it by clicking on the plus icon to the left of the search bar, select "Add User-Defined Setting", name it `SWIFT_VERSION` and set it to `4.2`.

![Swift version](Images/Swift_Version.png)

Install the native HyperTrack SDK through CocoaPods:

```bash
pod install
```

##### Manually link the wrapper code.

After Cocoapods is finished installing dependencies, we need to manually link the wrapper code. This is a **required** step, in addition to the link command described previously.

1. Open the iOS module files directory, located inside `node_modules/hypertrack-sdk-react-native/ios/`.
2. Open the app's workspace file (`.xcworkspace`) in Xcode.
3. Move the `RNHyperTrack.h` and `RNHyperTrack.m` files to your project. When shown a popup window, select `Create groups`.

![Linking on iOS](Images/link.gif)

#### Android

##### Update compileSdkVersion, buildToolsVersion, support library version

Edit the `build.gradle` file in your `android/app` directory:

```groovy
android {
    compileSdkVersion 28
    ...
}
```

```groovy
dependencies {
    ...
    compile project(':hypertrack-sdk-react-native')
    compile fileTree(dir: "libs", include: ["*.jar"])
    compile "com.android.support:appcompat-v7:26.1.0"
    compile "com.facebook.react:react-native:+"  // From node_modules
    ...
}
```

##### Add maven for Google Play Service Libraries

Edit the `build.gradle` file in your `android` directory:

```groovy
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        jcenter()
        maven {
            url 'https://maven.google.com/'
            name 'Google'
        }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.1'
        classpath 'com.google.gms:google-services:3.1.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        mavenLocal()
        maven {
            name 'hypertrack'
            url 'http://m2.hypertrack.com'
        }        
        jcenter()
        maven {
            // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
            url "$rootDir/../node_modules/react-native/android"
        }
        maven {
            url 'https://maven.google.com/'
            name 'Google'
        }
    }
}
```

### Step 3: Import the module

```js
import { NativeModules } from 'react-native';
var RNHyperTrack = NativeModules.RNHyperTrack;
```

### Step 4: Configure the Publishable Key

To configure the SDK, set the [publishable key](#publishable-key). This can be done in the `constructor` of your component class.

```js
export default class App extends Component {
  constructor() {
    super();
    RNHyperTrack.initialize('YOUR_PUBLISHABLE_KEY');
  }
}
```

### Step 5: Enable native capabilities on iOS

#### Enable background location updates

Enable Background Modes in your project target's Capabilities tab. Choose "Location updates".

![Capabilities tab in Xcode](Images/Background_Modes.png)

#### Add purpose strings

Set the following purpose strings in the `Info.plist` file:

![Always authorization location](Images/Always_Authorization.png)

Include `Privacy - Location Always Usage Description` key only when you need iOS 10 compatibility.

You can ask for "When In Use" permission only, but be advised that the device will see a blue bar at the top while your app is running.

![In use authorization location](Images/In_Use_Authorization.png)

Be advised, purpose strings are mandatory, and the app crashes without them.

### You are all set

You can now run the app and start using HyperTrack. You can see your devices on the [dashboard](#dashboard).

## Dashboard

Once your app is running, go to the [dashboard](https://dashboard.hypertrack.com/devices) where you can see a list of all your devices and their live location with ongoing activity on the map.

![Dashboard](Images/Dashboard.png)


## Frequently Asked Questions

### iOS
- [What iOS versions are supported](#supported-versions-on-ios)
- [Error: Access to Activity services has not been authorized](#error-access-to-activity-services-has-not-been-authorized)

#### Supported versions on iOS

Currently we do support all of the iOS versions starting from iOS 10.

#### Error: Access to Activity services has not been authorized
You are running the quickstart app on the iOS simulator, which currently does not support CoreMotion services. You can test the quickstart app on real iOS devices only.

### Android
- [What API levels (Android versions) are supported](#supported-versions-on-android)
- [NoClassDefFoundError](#javalangnoclassdeffounderror)
- [Dependencies Conflicts](#dependencies)
- [Persistent notification](#persistent-notification)
- [Handling custom ROMs](#handling-custom-roms)
- [HyperTrack notification shows even after app is terminated](#hypertrack-notification-shows-even-after-my-app-is-terminated)

#### Supported versions on Android
Currently we do support all of the Android versions starting from API 19 (Android 4.4 Kit Kat)

#### java.lang.NoClassDefFoundError
I've added SDK and my app started failing with message like `Fatal Exception: java.lang.NoClassDefFoundError`.
The reason of it, is that on Android API level 19 and below you cannot have more than 65536 methods in your app (including libraries methods). Please, check [this Stackoverflow](https://stackoverflow.com/questions/34997835/fatal-exception-java-lang-noclassdeffounderror-when-calling-static-method-in-an) answer for solutions.

#### Dependencies
SDK dependencies graph looks like below:

    +--- com.android.volley:volley:1.1.0
    +--- com.google.code.gson:gson:2.8.5
    +--- org.greenrobot:eventbus:3.1.1
    +--- com.parse.bolts:bolts-tasks:1.4.0
    +--- net.grandcentrix.tray:tray:0.12.0
    |    \--- com.android.support:support-annotations:23.0.1 -> 28.0.0
    +--- com.google.android.gms:play-services-location:16.0.0
    |    +--- com.google.android.gms:play-services-base:16.0.1
    |    |    +--- com.google.android.gms:play-services-basement:16.0.1
    |    |    |    \--- com.android.support:support-v4:26.1.0
    |    |    |         +--- com.android.support:support-compat:26.1.0
    |    |    |         |    +--- com.android.support:support-annotations:26.1.0 -> 28.0.0
    |    |    |         |    \--- android.arch.lifecycle:runtime:1.0.0
    |    |    |         |         +--- android.arch.lifecycle:common:1.0.0
    |    |    |         |         \--- android.arch.core:common:1.0.0
    |    |    |         +--- com.android.support:support-media-compat:26.1.0
    |    |    |         |    +--- com.android.support:support-annotations:26.1.0 -> 28.0.0
    |    |    |         |    \--- com.android.support:support-compat:26.1.0 (*)
    |    |    |         +--- com.android.support:support-core-utils:26.1.0
    |    |    |         |    +--- com.android.support:support-annotations:26.1.0 -> 28.0.0
    |    |    |         |    \--- com.android.support:support-compat:26.1.0 (*)
    |    |    |         +--- com.android.support:support-core-ui:26.1.0
    |    |    |         |    +--- com.android.support:support-annotations:26.1.0 -> 28.0.0
    |    |    |         |    \--- com.android.support:support-compat:26.1.0 (*)
    |    |    |         \--- com.android.support:support-fragment:26.1.0
    |    |    |              +--- com.android.support:support-compat:26.1.0 (*)
    |    |    |              +--- com.android.support:support-core-ui:26.1.0 (*)
    |    |    |              \--- com.android.support:support-core-utils:26.1.0 (*)
    |    |    \--- com.google.android.gms:play-services-tasks:16.0.1
    |    |         \--- com.google.android.gms:play-services-basement:16.0.1 (*)
    |    +--- com.google.android.gms:play-services-basement:16.0.1 (*)
    |    +--- com.google.android.gms:play-services-places-placereport:16.0.0
    |    |    \--- com.google.android.gms:play-services-basement:16.0.1 (*)
    |    \--- com.google.android.gms:play-services-tasks:16.0.1 (*)
    \--- com.android.support:support-annotations:28.0.0

Common problem here is depending on different versions of `com.android.support` library components. You can explicitly specify required version by adding it as a dependency in your app's `build.gradle`, e.g.:
```
  implementation `com.android.support:support-v4:28.0.0`
```
That will take precedence over SDK version and you'll have one version of support library on your classpath.

#### Persistent notification
HyperTrack SDK, by default, runs as a foreground service. This is to ensure that the location tracking works reliably even when your app is minimized. A foreground service is a service that the user is actively aware of and isn't a candidate for the system to kill when low on memory.
Android mandates that a foreground service provides a persistent notification in the status bar. This means that the notification cannot be dismissed by the user.

![persistent-notification](https://user-images.githubusercontent.com/10487613/54007190-6ec47c00-4115-11e9-9743-332befbcf8f5.png)

#### Handling custom ROMs
Smartphones are getting more and more powerful, but the battery capacity is lagging behind. Device manufactures are always trying to squeeze some battery saving features into the firmware with each new Android release. Manufactures like Xiaomi, Huawei and OnePlus have their own battery savers that kills the services running in the background.
To avoid OS killing the service, users of your app need to override the automatic battery management and set it manual. To inform your users and direct them to the right setting page, you may add the following code in your app. This would intent out your user to the right settings page on the device.

```java
try {
    Intent intent = new Intent();
    String manufacturer = android.os.Build.MANUFACTURER;
    if ("xiaomi".equalsIgnoreCase(manufacturer)) {
        intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
    }
    else if ("oppo".equalsIgnoreCase(manufacturer)) {
        intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
    }
    else if ("vivo".equalsIgnoreCase(manufacturer)) {
        intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
    }

    List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    if  (list.size() > 0) {
        context.startActivity(intent);
    }
    }
    catch (Exception e) {
        Crashlytics.logException(e);
}
```
You may also try out open source libraries like https://github.com/judemanutd/AutoStarter.

Some manufacturers don't allow to whitelist apps programmatically. In that case the only way to achieve service reliability is manual setup. E.g. for Oxygen OS (OnePlus) you need to select *Lock* menu item from app options button in _Recent Apps_ view:

![one-plus-example](https://user-images.githubusercontent.com/10487613/58070846-388a8a80-7ba3-11e9-8b4f-11e39d26382b.png)


#### HyperTrack notification shows even after my app is terminated
The HyperTrack service runs as a separate component and it is still running when the app that started it is terminated. That is why you can observe that notification. When you stop tracking (`stopTracking()`), the notification goes away.

## Support
Join our [Slack community](http://slack.hypertrack.com) for instant responses. You can also email us at help@hypertrack.com.

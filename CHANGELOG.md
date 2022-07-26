# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [7.19.1] - 2022-07-27
### Fixed
- Fixed React Native C++ incompatibility issue with Android SDK 6.2.0

## [7.19.0] - 2022-07-18
### Changed
- Updated HyperTrack Android SDK to 6.2.0 and iOS SDK to 4.12.4

## [7.18.4] - 2022-06-15
### Changed
- Updated HyperTrack Android SDK to 6.1.4

## [7.18.3] - 2022-06-15
### Changed
- Updated HyperTrack Android SDK to 6.1.3

## [7.18.2] - 2022-06-13
### Changed
- Updated HyperTrack iOS SDK to 4.12.3

## [7.18.1] - 2022-06-10
### Changed
- Updated HyperTrack iOS SDK to 4.12.1 to fix a regression in tracking listener

## [7.18.0] - 2022-06-09
### Changed
- Updated HyperTrack Android SDK to 6.1.2 and iOS SDK to 4.12.0

## [7.17.0] - 2022-06-03
### Changed
- Updated HyperTrack Android SDK to 6.1.1

## [7.16.0] - 2022-05-03
### Changed
- Updated HyperTrack Android SDK to [6.0.4](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#604---2022-04-29)

## [7.16.0] - 2022-05-03
### Changed
- Updated HyperTrack Android SDK to [6.0.4](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#604---2022-04-29)

## [7.15.0] - 2022-04-29
### Added
- Current location getter returns either the current location of the device or an outage reason for why current location is unavailable.

## [7.14.0] - 2022-03-24
### Changed
- Updated HyperTrack Android SDK to [6.0.2](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#602---2022-03-18)

## [7.13.0] - 2022-01-05
### Changed
- Updated iOS SDK to [4.9.0](https://github.com/hypertrack/sdk-ios/blob/master/CHANGELOG.md#490---2022-01-05)

## [7.12.1] - 2021-11-11
### Changed
- Updated Android SDK to [5.4.5](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#545---2021-11-05)

## [7.12.0] - 2021-10-29
### Changed
- Updated Android SDK to [5.4.4](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#544---2021-10-29)

## [7.11.0] - 2021-10-28
### Changed
- Updated Android SDK to [5.4.3](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#543---2021-09-01)

## [7.10.0] - 2021-08-04
### Changed
- Updated Android SDK to [5.4.0](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#540---2021-08-04)

## [7.9.0] - 2021-07-07
### Changed
- Updated Android SDK to [v5.2.5](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#525---2021-07-02)

## [7.8.1] - 2021-06-11
### Fixed
- Accidently removed import added back.

## [7.8.0] - 2021-06-11
### Changed
- Updated Android SDK to [v5.2.2](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#522---2021-06-11)

## [7.7.0] - 2021-05-29
### Changed
- Updated Android SDK to [v5.1.0](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#510---2021-05-28)

## [7.6.0] - 2021-05-07
### Changed
- Updated Android SDK to [v4.12.0](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#4120---2021-05-07)

## [7.5.0] - 2021-04-26
### Changed
- Updated iOS SDK to [4.8.0](https://github.com/hypertrack/sdk-ios/blob/master/CHANGELOG.md#480---2021-04-22)
- Updated Android SDK to [v4.11.1](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#4111---2021-04-20)

## [7.4.0] - 2021-04-02
### Changed
- Updated Android SDK to [v4.11.0](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#4110---2021-03-30)
### Fixed
- SDK won't navigate to Settings showing "Select `Allow Always` option" snackbar on Android 11 and later.

## [7.3.0] - 2021-03-04
### Changed
- Updated Android SDK to [v4.10.0](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#4100---2021-02-22)

## [7.2.0] - 2020-12-23
### Changed
- Updated iOS SDK to [v4.7.0](https://github.com/hypertrack/sdk-ios/blob/master/CHANGELOG.md#470---2020-12-23)
### Fixed
- iOS plugin runtime error notification.

## [7.1.0] - 2020-11-17
### Changed
- Updated Anroid SDK to [v4.8.0](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#480---2020-10-30)
- Updated iOS SDK to [v4.6.0](https://github.com/hypertrack/sdk-ios/blob/master/CHANGELOG.md#460---2020-11-09)
- Using React-Core on iOS (https://github.com/hypertrack/sdk-react-native/pull/19)

## [7.0.1] - 2020-10-07
### Fixed
- Added `hypertrack-sdk-react-native.podspec` to `package.json` file.
### Changed
- Updated iOS SDK to `4.5.1`.

## [7.0.0] - 2020-10-02
### Changed
- Updated react-native to version `0.63.3`.
- Updated Android SDK to `4.6.0`.
### Removed
- Remove startsTracking API from createInstance.

### API Diff
```diff
/**
 * Initialize the HyperTrack SDK
 * @param {string} publishableKey - A unique String that is used to identify your account with HyperTrack.
- * @param {boolean} startsTracking - Should the SDK start tracking immediately after initialization.
 * @param {boolean} automaticallyRequestPermissions - If true, SDK automatically triggers location and motion activity permissions dialogs when tracking starts.
 */
- async createInstance(publishableKey, startsTracking = true, automaticallyRequestPermissions = true): Promise<HyperTrackAPI> {
-    await HyperTrack.initialize(publishableKey, startsTracking, automaticallyRequestPermissions);
+ async createInstance(publishableKey, automaticallyRequestPermissions = true): Promise<HyperTrackAPI> {
+    await HyperTrack.initialize(publishableKey, false, automaticallyRequestPermissions);
    return new HyperTrackAPI();
},
```

## [6.6.0] - 2020-07-24
### Changed
- Updated react-native to version `0.61.4`.
- Updated Android SDK to `4.5.2`.

## [6.5.1] - 2020-07-20
### Changed
- Updated Android SDK to `4.5.1`.

## [6.5.0] - 2020-07-16
### Changed
- Updated Android SDK to `4.5.0`.

## [6.4.0] - 2020-07-14
### Added
- Only send events when there are listeners in iOS wrapper.
### Changed
- Reject promise properly and with error code during iOS initialization.
### Fixed
- Fixed regression where syncDeviceSettings was removed from iOS SDK wrapper.

## [6.3.0] - 2020-07-13
### Changed
- Control permission dialogs on iOS.

### API Diff

```diff
/**
 * Initialize the HyperTrack SDK
 * @param {string} publishableKey - A unique String that is used to identify your account with HyperTrack.
 * @param {bool} startsTracking - Should the SDK start tracking immediately after initialization.
+ * @param {boolean} automaticallyRequestPermissions - If true, SDK automatically triggers location and motion activity permissions dialogs when tracking starts.
 */

- async createInstance(publishableKey, startsTracking = true): Promise<HyperTrackAPI> {
+ async createInstance(publishableKey, startsTracking = true, automaticallyRequestPermissions = true): Promise<HyperTrackAPI> {
-  await HyperTrack.initialize(publishableKey, startsTracking);
+  await HyperTrack.initialize(publishableKey, startsTracking, automaticallyRequestPermissions);
   return new HyperTrackAPI();
 },
```

## [6.2.2] - 2020-04-14
### Changed
- Updated Android dependency repos.

## [6.2.1] - 2020-01-08
### Added
- RN SDK wrapper now supports native iOS `4.0.x` SDK.

## [6.1.0] - 2019-09-30
### Added
- Adding sync device settings
- Updated Android SDK to  `3.4.7`.

## [6.0.0] - 2019-09-17
### Changed
- Updated interface with promises and split name and metadata
- Updated react-native to version `0.59.9`.

## [5.0.5] - 2019-08-29
### Changed
- Updated Android SDK to  `3.4.5`.

## [5.0.4] - 2019-08-23
### Changed
- Updated Android SDK to `3.3.4` and stop tracking events.

## [5.0.3] - 2019-08-22
### Added
- Make deviceID change with publishable key.

## [5.0.2] - 2019-08-15
### Fixed
- Fixed Android wrapper permissions handling in initialize and startTracking.

## [5.0.1] - 2019-08-15
### Added
- Expose critical errors and update the integration.

## [4.1.1] - 2019-08-08
### Changed
- Bump Android SDK version to `3.3.1`.

## [4.1.0] - 2019-08-07
### Changed
- Make `getDeviceID` consistent between Android and iOS wrappers.

## [4.0.2] - 2019-08-06
### Fixed
- Fixed null context exception in Android wrapper.

## [4.0.1] - 2019-07-29
### Added
- Added an option to start tracking right after initialization.

### API Diff

```diff
/**
 * Initialize the HyperTrack SDK
 * @param {string} publishableKey - A unique String that is used to identify your account with HyperTrack.
+ * @param {bool} startsTracking - Should the SDK start tracking immediately after initialization
 */
 initialize(publishableKey) {
-  RNHyperTrack.initialize(publishableKey);
+  RNHyperTrack.initialize(publishableKey, startsTracking);
 },
```

## [3.0.0] - 2019-07-23
### Added
- Backups key for Android SDK
### Changed
- Bump Android SDK version to 3.3.0 and switch to trip markers for native calls.

## [2.0.0] - 2019-07-17
### Deprecated
- `sendCustomEvent` was renamed to `setTripMarker`.

## [1.0.2] - 2019-07-04
### Added
- Added silent push notifications.

## [1.0.1] - 2019-06-26
### Added
- Added HyperTrack repository to repos list.

## [1.0.0] - 2019-06-07
Initial release.


[7.19.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.19.1
[7.19.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.19.0
[7.18.4]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.18.4
[7.18.3]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.18.3
[7.18.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.18.2
[7.18.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.18.1
[7.18.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.18.0
[7.17.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.17.0
[7.16.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.16.0
[7.15.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.15.0
[7.14.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.14.0
[7.13.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.13.0
[7.12.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.12.1
[7.12.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.12.0
[7.11.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.11.0
[7.10.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.10.0
[7.9.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.9.0
[7.8.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.8.1
[7.8.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.8.0
[7.7.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.7.0
[7.6.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.6.0
[7.5.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.5.0
[7.4.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.4.0
[7.3.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.3.0
[7.2.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.2.0
[7.1.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.1.0
[7.0.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.0.1
[7.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.0.0
[6.6.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.6.0
[6.5.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.5.1
[6.5.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.5.0
[6.4.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.4.0
[6.3.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.3.0
[6.2.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.2.2
[6.2.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.2.1
[6.1.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.1.0
[6.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.0.0
[5.0.5]: https://github.com/hypertrack/sdk-react-native/releases/tag/5.0.5
[5.0.4]: https://github.com/hypertrack/sdk-react-native/releases/tag/5.0.4
[5.0.3]: https://github.com/hypertrack/sdk-react-native/releases/tag/5.0.3
[5.0.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/5.0.2
[5.0.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/5.0.1
[4.1.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/4.1.1
[4.1.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/4.1.0
[4.0.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/4.0.2
[4.0.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/4.0.1
[3.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/3.0.0
[2.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/2.0.0
[1.0.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/1.0.2
[1.0.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/1.0.1
[1.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/1.0.0

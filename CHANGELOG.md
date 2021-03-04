# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [7.3.0] - 2020-03-04
### Changed
- Updated Android SDK to [v4.10.0](https://github.com/hypertrack/sdk-android/blob/master/CHANGELOG.md#4100---2020-02-22)

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

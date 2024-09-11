# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [13.6.1] - 2024-09-11

### Changed

- Updated HyperTrack SDK Android to [7.8.2](https://github.com/hypertrack/sdk-android/releases/tag/7.8.2)

## [13.6.0] - 2024-09-05

### Added

- Support for Motion & Activity detection
  - If your app asks for the Motion & Activity permission (for iOS) or the Activity Recognition permission (for Android) and the user grants it, you will have better activity detection in polylines
  - Use new Activity service plugin `hypertrack-sdk-react-native-plugin-android-activity-service-google` (See [Plugins](https://hypertrack.com/docs/plugins))

### Changed

- Updated HyperTrack SDK iOS to [5.8.0](https://github.com/hypertrack/sdk-ios/releases/tag/5.8.0)
- Updated HyperTrack SDK Android to [7.8.0](https://github.com/hypertrack/sdk-android/releases/tag/7.8.0)

## [13.5.1] - 2024-09-02

### Fixed

- Serialization issue with `Order.isInsideGeofence`

## [13.5.0] - 2024-08-21

### Added

- Support for on-device geofencing via new `HyperTrack.orders.get("my_order_handle").isInsideGeofence` property
  - To learn more about how to best use this new feature see our guide here: [Verify shift presence before starting work](https://developer.hypertrack.com/docs/clock-in-out-tagging#verify-shift-presence-before-starting-work)

Example use for worker clock in:

```typescript
// check worker presence synchronously
let activeOrders = await HyperTrack.getOrders()
let currentOrder = activeOrders.get("current_order")
if (currentOrder !== undefined) { handlePresence(currentOrder) }
else { console.log("'current_order' not found") }

// or subscribe to the changes in orders to get the status updates
HyperTrack.subscribeToOrders(orders => {
  let let currentOrder = activeOrders.get("current_order")
  if (currentOrder !== undefined) { handlePresence(currentOrder) }
  else { console.log("'current_order' not found") }
})

// handle worker presence inside the order destination geofence
function handlePresence(isInsideGeofence: Result<boolean, LocationError>) {
  switch (isInsideGeofence.type) {
    case 'success':
      if (isInsideGeofence.value) {
        // allow worker to clock in for the shift
      } else {
        // "to clock in you must be at order destination"
      }
      break;
    case 'failure':
      // resolve errors to check for presence
      break;
  }
}
```

### Changed

- Updated HyperTrack SDK iOS to [5.7.0](https://github.com/hypertrack/sdk-ios/releases/tag/5.7.0)
- Updated HyperTrack SDK Android to [7.7.0](https://github.com/hypertrack/sdk-android/releases/tag/7.7.0)

## [13.4.0] - 2024-06-13

### Added

- New `setWorkerHandle` and `getWorkerHandle` can be used to identify workers
  - We observed our customers identify worker devices via `HyperTrack.metadata`, so we decided to make it a first class citizen in our API.
  - If you previously used `metadata` to identify workers, we suggest using `workerHandle` for this purpose instead.

### Changed

- Updated HyperTrack SDK Android to [7.6.0](https://github.com/hypertrack/sdk-android/releases/tag/7.6.0)

## [13.3.5] - 2024-06-05

### Changed

- Updated HyperTrack SDK iOS to [5.6.0](https://github.com/hypertrack/sdk-ios/releases/tag/5.6.0)

## [13.3.4] - 2024-05-24

### Changed

- Updated HyperTrack SDK Android to [7.5.5](https://github.com/hypertrack/sdk-android/releases/tag/7.5.5)

## [13.3.3] - 2024-05-14

### Changed

- Updated HyperTrack SDK iOS to [5.5.4](https://github.com/hypertrack/sdk-ios/releases/tag/5.5.4)
- Updated HyperTrack SDK Android to [7.5.4](https://github.com/hypertrack/sdk-android/releases/tag/7.5.4)

## [13.3.2] - 2024-04-30

### Changed

- Updated HyperTrack SDK iOS to [5.5.3](https://github.com/hypertrack/sdk-ios/releases/tag/5.5.3)

## [13.3.1] - 2024-04-24

### Changed

- Updated HyperTrack SDK iOS to [5.5.2](https://github.com/hypertrack/sdk-ios/releases/tag/5.5.2)
- Updated HyperTrack SDK Android to [7.5.3](https://github.com/hypertrack/sdk-android/releases/tag/7.5.3)

## [13.3.0] - 2024-04-19

### Added

- New `addGeotag` method that have `orderHandle` and `orderStatus` parameters. You can use this API when users need to clock in/out of work in your app to honor their work time (see [Clock in/out Tagging](https://hypertrack.com/docs/clock-inout-tracking#add-clock-inout-events-to-a-shift-timeline) guide for more info)

### Changed

- Updated HyperTrack SDK iOS to [5.5.1](https://github.com/hypertrack/sdk-ios/releases/tag/5.5.0)
- Updated HyperTrack SDK Android to [7.5.2](https://github.com/hypertrack/sdk-android/releases/tag/7.5.1)

## [13.2.3] - 2024-02-27

### Changed

- Updated HyperTrack SDK Android to [7.4.3](https://github.com/hypertrack/sdk-android/releases/tag/7.4.3)

## [13.2.2] - 2024-02-14

### Changed

- Updated HyperTrack SDK Android to [7.4.2](https://github.com/hypertrack/sdk-android/releases/tag/7.4.2)

## [13.2.1] - 2024-02-13

### Changed

- Updated HyperTrack SDK iOS to [5.4.1](https://github.com/hypertrack/sdk-ios/releases/tag/5.4.1)
- Updated HyperTrack SDK Android to [7.4.1](https://github.com/hypertrack/sdk-android/releases/tag/7.4.1)

## [13.2.0] - 2024-02-06

### Fixed

- Fixed a serialization issue with dynamic publishable key implementation

## [13.1.0] - 2024-01-29

### Changed

- Updated HyperTrack SDK iOS to [5.4.0](https://github.com/hypertrack/sdk-ios/releases/tag/5.4.0)
- Updated HyperTrack SDK Android to [7.4.0](https://github.com/hypertrack/sdk-android/releases/tag/7.4.0)

## [13.0.0] - 2024-01-24

### Added

- New Dynamic publishable key API allows to set the publishable key later in the app lifecycle. It should be used only when there is no other way around it and it's impossible to know the publishable key in advance at the build time. Please contact us if this matches your use case

### Changed

- Updated HyperTrack SDK iOS to [5.3.0](https://github.com/hypertrack/sdk-android/releases/tag/5.3.0)
- Updated HyperTrack SDK Android to [7.3.0](https://github.com/hypertrack/sdk-android/releases/tag/7.3.0)

### Fixed

- Remove async from `setIsAvailable`, `setIsTracking`, `setMetadata`, `setName` methods

## [12.1.0] - 2024-01-17

### Fixed

- Mitigates SDK cache file corruption that led to some Android devices loading to `device_id` values with all zeroes.
- [iOS] Optimized querying the Location Manager to reduce impact on battery life and main thread performance.
- Reduced network traffic.

### Changed

- Updated HyperTrack Android SDK to [7.2.0](https://github.com/hypertrack/sdk-android/releases/tag/7.2.0)
- Updated HyperTrack iOS SDK to [5.2.0](https://github.com/hypertrack/sdk-ios/releases/tag/5.2.0)

## [12.0.0] - 2023-12-12

### Changed

- Now the SDK uses the Plugin architecture. You need to add additionall NPM dependencies for the plugins for the SDK to work properly. See [Plugins](https://hypertrack.com/docs/plugins) and [Migration Guide](https://hypertrack.com/docs/install-sdk-react-native#migration-guide) for more information.
- Updated HyperTrack Android SDK to [7.0.11](https://github.com/hypertrack/sdk-android/releases/tag/7.0.10)

## [11.0.11] - 2023-12-11

### Changed

- Updated HyperTrack Android SDK to [7.0.10](https://github.com/hypertrack/sdk-android/releases/tag/7.0.10)
- Updated HyperTrack iOS SDK to [5.0.8](https://github.com/hypertrack/sdk-ios/releases/tag/5.0.8)

## [11.0.10] - 2023-11-20

### Changed

- Updated HyperTrack Android SDK to [7.0.9](https://github.com/hypertrack/sdk-android/releases/tag/7.0.9)
- Updated HyperTrack iOS SDK to [5.0.7](https://github.com/hypertrack/sdk-ios/releases/tag/5.0.7)

## [11.0.9] - 2023-11-10

### Changed

- Updated HyperTrack Android SDK to [7.0.8](https://github.com/hypertrack/sdk-android/releases/tag/7.0.8)
- Updated HyperTrack iOS SDK to [5.0.6](https://github.com/hypertrack/sdk-ios/releases/tag/5.0.6)

## [11.0.8] - 2023-11-08

### Changed

- Updated HyperTrack Android SDK to [7.0.7](https://github.com/hypertrack/sdk-android/releases/tag/7.0.7)
- Updated HyperTrack iOS SDK to [5.0.5](https://github.com/hypertrack/sdk-ios/releases/tag/5.0.5)

## [11.0.7] - 2023-10-19

### Fixed

- Lazily initialize event listener subscriptions

## [11.0.6] - 2023-10-12

### Changed

- Updated HyperTrack iOS SDK to [5.0.4](https://github.com/hypertrack/sdk-ios/releases/tag/5.0.3)

## [11.0.5] - 2023-10-10

### Changed

- Updated HyperTrack Android SDK to [7.0.6](https://github.com/hypertrack/sdk-android/releases/tag/7.0.6)
- Updated HyperTrack iOS SDK to [5.0.3](https://github.com/hypertrack/sdk-ios/releases/tag/5.0.3)

## [11.0.4] - 2023-10-06

### Changed

- Updated HyperTrack Android SDK to [7.0.5](https://github.com/hypertrack/sdk-android/releases/tag/7.0.5)

## [11.0.3] - 2023-10-04

### Changed

- Updated HyperTrack Android SDK to [7.0.4](https://github.com/hypertrack/sdk-android/releases/tag/7.0.4)

## [11.0.2] - 2023-09-28

### Changed

- Updated HyperTrack Android SDK to [7.0.3](https://github.com/hypertrack/sdk-android/releases/tag/7.0.3)
- Updated HyperTrack iOS SDK to [5.0.2](https://github.com/hypertrack/sdk-ios/releases/tag/5.0.2)

## [11.0.1] - 2023-09-20

### Fixed

- Fixed setting wrong object format for `setMetadata()`

## [11.0.0] - 2023-09-14

### Added

- `locate()` to ask for one-time user location
- `subscribeToLocation()` to subscribe to user location updates
- `getErrors()`
- `getName()`
- `getMetadata()`
- HyperTrackError types:
  - `noExemptionFromBackgroundStartRestrictions`
  - `permissionsNotificationsDenied`

### Changed

- Updated HyperTrack Android SDK to [7.0.1](https://github.com/hypertrack/sdk-android/releases/tag/7.0.1)
- Add Android SDK plugins (`location-services-google-19-0-1` and `push-service-firebase`)
- Updated HyperTrack iOS SDK to [5.0.1](https://github.com/hypertrack/sdk-ios/releases/tag/5.0.2)
- The whole HyperTrack API is now static
- Changed the way to provide publishableKey (you need to add `HyperTrackPublishableKey` `meta-data` item to your `AndroidManifest.xml`)
- Renamed HyperTrackError types:
  - `gpsSignalLost` to `locationSignalLost`
  - `locationPermissionsDenied` to `permissionsLocationDenied`
  - `locationPermissionsInsufficientForBackground` to `permissionsLocationInsufficientForBackground`
  - `locationPermissionsNotDetermined` to `permissionsLocationNotDetermined`
  - `locationPermissionsProvisional` to `locationPermissionsProvisional`
  - `locationPermissionsReducedAccuracy` to `permissionsLocationReducedAccuracy`
  - `locationPermissionsRestricted` to `permissionsLocationRestricted`
- Renamed `isAvailable()` to `getIsAvailable()`
- Renamed `isTracking()` to `getIsTracking()`
- Renamed `setAvailability()` to `setIsAvailable(boolean)`
- Changed `startTracking()` and `stopTracking()` to `setIsTracking(boolean)`
- Renamed `subscribeToTracking()` to `subscribeToIsTracking()`
- Renamed `subscribeToAvailability()` to `subscribeToIsAvailable()`

### Removed

- `initialize()` method (the API is now static)
- `SdkInitParams` (the config now should be done with the `AndroidManifest` metadata and `Info.plist`)
- Motion Activity permissions are not required for tracking anymore
- HyperTrackError types:
  - `motionActivityPermissionsDenied`
  - `motionActivityServicesDisabled`
  - `motionActivityServicesUnavailable`
  - `motionActivityPermissionsRestricted`
  - `networkConnectionUnavailable`
- `sync()` method

## [10.0.3] - 2023-08-04

### Fixed

- Propagating Android exception stacktrace to JS API

## [10.0.2] - 2023-06-16

### Changed

- Updated HyperTrack iOS SDK to 4.16.1

## [10.0.1] - 2023-06-13

### Changed

- Updated HyperTrack Android SDK to 6.4.2

## [10.0.0] - 2023-06-06

### Changed

- Updated HyperTrack Android SDK to 6.4.1
- `getLocation` return type to `Result<Location, LocationError>`

### Fixed

- `addGeotag` and `getLocation` wrong return type that is different from the one in the API docs (introduced in 9.2.0)

## [9.2.2] - 2023-06-01

### Changed

- Updated HyperTrack iOS SDK to 4.16.0

## [9.2.1] - 2023-05-19

### Fixed

- Fixed `addGeotag` (with expected location) return type

## [9.2.0] - 2023-05-18

### Changed

- **Breaking change:** Changed `addGeotag` return type to `Result<Location, LocationError>`
- HyperTrack iOS SDK updated to 4.15.0

### Added

- `addGeotag` with expected location

## [9.1.1] - 2023-03-30

### Fixed

- Removed types re-export to fix issues with some build configurations

## [9.1.0] - 2023-03-21

### Added

- `automaticallyRequestPermissions` param to `initialize()`

## [9.0.0] - 2023-02-17

### Changed

- Updated HyperTrack iOS SDK to 4.14.0
- Updated HyperTrack Android SDK to 6.4.0
- `createInstance()` renamed to `initialize()`
- `getDeviceID()` renamed to `getDeviceId()`
- `syncDeviceSettings()` renamed to `sync()`
- `setDeviceName()` renamed to `setName()`
- `subscribeToErrors()` callback now has param type (`HyperTrackError`)
- `getLocation` return type
- `LocationError` now has different structure

### Added

- `initialize()` configuration params for
  - Debug logging
  - Background location permissions request for Android
  - Mock locations
- `subscribeToAvailability()`

### Removed

- `automaticallyRequestPermissions` param from `initialize()`
- `enableDebugLogging()` (use `initialize()` param `loggingEnabled` istead)
- `setTripMarker()` (use `addGeotag` instead)

## [8.2.2] - 2022-10-10

### Fixed

- Deployment target for iOS changed to 11, to comply with native iOS SDK.

## [8.2.1] - 2022-09-27

### Fixed

- `enableDebugLogging()` now doesn't cause an infinite loop when called.

## [8.2.0] - 2022-09-23

### Changed

- Updated Android SDK to 6.3.0 and iOS SDK to 4.13.0
- When the Android app is uninstalled completely from the device and then installed back, the device_id will change. This improves the stability of the data coming from the SDK. Now this behavior is the same between Android SDK and iOS SDK.
- If the user simulates locations when it's prohibited, the SDK still passes real locations through if there are any.
- Decreased the time sensitivity for first location detection. Results in fewer `location_unavailable` outages when tracking starts on Android.

### Fixed

- Now, all logs in Android SDK are disabled by default and can be enabled by enableDebugLogging()

## [8.1.0] - 2022-08-30

### Changed

- Updated Android SDK to 6.2.2 that is compabible with any native library (React Native C++) and reduces the SDK size too. SDK should now add only 4MB to the compressed app's size, when downloaded from Google Play instead of 17 MB on 8.0.0.

## [8.0.0] - 2022-08-05

### Added

- New interface for listeners.
- Reference documentation.
- TypeScript support.

### Changed

- Updated React Native from 0.63.3 to 0.69.1

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
- Updated Android SDK to `3.4.7`.

## [6.0.0] - 2019-09-17

### Changed

- Updated interface with promises and split name and metadata
- Updated react-native to version `0.59.9`.

## [5.0.5] - 2019-08-29

### Changed

- Updated Android SDK to `3.4.5`.

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

[1.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/1.0.0
[1.0.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/1.0.1
[1.0.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/1.0.2
[2.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/2.0.0
[3.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/3.0.0
[4.0.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/4.0.1
[4.0.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/4.0.2
[4.1.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/4.1.0
[4.1.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/4.1.1
[5.0.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/5.0.1
[5.0.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/5.0.2
[5.0.3]: https://github.com/hypertrack/sdk-react-native/releases/tag/5.0.3
[5.0.4]: https://github.com/hypertrack/sdk-react-native/releases/tag/5.0.4
[5.0.5]: https://github.com/hypertrack/sdk-react-native/releases/tag/5.0.5
[6.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.0.0
[6.1.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.1.0
[6.2.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.2.1
[6.2.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.2.2
[6.3.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.3.0
[6.4.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.4.0
[6.5.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.5.0
[6.5.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.5.1
[6.6.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/6.6.0
[7.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.0.0
[7.0.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.0.1
[7.1.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.1.0
[7.2.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.2.0
[7.3.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.3.0
[7.4.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.4.0
[7.5.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.5.0
[7.6.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.6.0
[7.7.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.7.0
[7.8.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.8.0
[7.8.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.8.1
[7.9.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.9.0
[7.10.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.10.0
[7.11.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.11.0
[7.12.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.12.0
[7.12.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.12.1
[7.13.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.13.0
[7.14.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.14.0
[7.15.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.15.0
[7.16.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.16.0
[7.17.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.17.0
[7.18.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.18.0
[7.18.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.18.1
[7.18.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.18.2
[7.18.3]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.18.3
[7.18.4]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.18.4
[7.19.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.19.0
[7.19.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/7.19.1
[8.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/8.0.0
[8.1.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/8.1.0
[8.2.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/8.2.0
[8.2.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/8.2.1
[8.2.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/8.2.2
[9.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/9.0.0
[9.1.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/9.1.0
[9.1.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/9.1.1
[9.2.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/9.2.0
[9.2.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/9.2.1
[9.2.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/9.2.2
[10.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/10.0.0
[10.0.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/10.0.1
[10.0.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/10.0.2
[10.0.3]: https://github.com/hypertrack/sdk-react-native/releases/tag/10.0.3
[11.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.0
[11.0.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.1
[11.0.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.2
[11.0.3]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.3
[11.0.4]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.4
[11.0.5]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.5
[11.0.6]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.6
[11.0.7]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.7
[11.0.8]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.8
[11.0.9]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.9
[11.0.10]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.10
[11.0.11]: https://github.com/hypertrack/sdk-react-native/releases/tag/11.0.11
[12.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/12.0.0
[12.1.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/12.1.0
[13.0.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.0.0
[13.1.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.1.0
[13.2.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.2.0
[13.2.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.2.1
[13.2.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.2.2
[13.2.3]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.2.3
[13.3.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.3.0
[13.3.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.3.1
[13.3.2]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.3.2
[13.3.3]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.3.3
[13.3.4]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.3.4
[13.3.5]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.3.5
[13.4.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.4.0
[13.5.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.5.0
[13.5.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.5.1
[13.6.0]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.6.0
[13.6.1]: https://github.com/hypertrack/sdk-react-native/releases/tag/13.6.1

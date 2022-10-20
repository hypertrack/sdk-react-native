/**
 * types that we receive from native code
 */

export type SDKDeviceID = { type: 'deviceID'; value: string };
export type SDKIsTracking = { type: 'isTracking'; value: boolean };
export type SDKIsAvailable = { type: 'isAvailable'; value: boolean };
export type SDKLocation = {
  type: 'location';
  value: {
    latitude: number;
    longitude: number;
  };
};

// enum naming convention is ignored to make datatype sync
// across platforms easier
export enum SDKHyperTrackError {
  gpsSignalLost = 'gpsSignalLost',
  locationPermissionsDenied = 'locationPermissionsDenied',
  locationPermissionsInsufficientForBackground = 'locationPermissionsInsufficientForBackground',
  locationServicesDisabled = 'locationServicesDisabled',
  motionActivityPermissionsDenied = 'motionActivityPermissionsDenied',
  invalidPublishableKey = 'invalidPublishableKey',
  permissionsDenied = 'permissionsDenied',
  blockedFromRunning = 'blockedFromRunning',
}

type SDKNotRunning = {
  type: 'notRunning';
};
type SDKStarting = {
  type: 'starting';
};
export type SDKErrors = {
  type: 'errors';
  errors: SDKHyperTrackError[];
};

export type SDKLocationError = SDKNotRunning | SDKStarting | SDKErrors;

/**
 * types that we return to JS
 */

export type Location = {
  latitude: number;
  longitude: number;
};

export type LocationRunningError =
  | 'notRunning' // The SDK is not collecting locations because it’s either not tracking or is unavailable.
  | 'starting'; // The SDK started collecting locations and is waiting for OS location services to respond.

export type HyperTrackError = keyof typeof SDKHyperTrackError;
// 'gpsSignalLost' // The user enabled mock location app while mocking locations is prohibited.
// 'locationPermissionsDenied' // Can’t start tracking in background with When In Use location permissions. SDK will automatically start tracking when app will return to foreground.
// 'locationPermissionsInsufficientForBackground' // The user has not chosen whether the app can use location services. SDK automatically asked for permissions.
// 'locationServicesDisabled' // The device doesn't have location services.
// 'motionActivityPermissionsDenied' // The user has not chosen whether the app can use motion activity services. SDK automatically asked for permissions.
// 'motionActivityServicesDisabled' // [iOS only] The device doesn't have motion activity services.
// 'invalidPublishableKey' // The publishable key is invalid.
// 'permissionsDenied' // The user has not chosen whether the app can use location services. SDK automatically asked for permissions.
// 'blockedFromRunning'; // The user has blocked the app from running in the background.

export type LocationError = LocationRunningError | HyperTrackError[];

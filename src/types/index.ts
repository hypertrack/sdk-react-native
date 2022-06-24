export interface Location {
  latitude: number;
  longitude: number;
}

enum LocationRunningError {
  NOT_RUNNING = 'notRunning', // The SDK is not collecting locations because it’s either not tracking or is unavailable.
  STARTING = 'starting', // The SDK started collecting locations and is waiting for OS location services to respond.
}

export type LocationError = LocationRunningError | Array<HyperTrackErrors>;

export interface Metadata {
  [key: string]: any;
}

export enum HyperTrackErrors {
  GPS_SIGNAL_LOST = 'gpsSignalLost', // The user enabled mock location app while mocking locations is prohibited.
  LOCATION_MOCKED = 'locationMocked', // The user denied location permissions.
  LOCATION_PERMISSIONS_DENIED = 'locationPermissionsDenied', // Can’t start tracking in background with When In Use location permissions. SDK will automatically start tracking when app will return to foreground.
  LOCATION_PERMISSIONS_INSUFFICIENT_FOR_BACKGROUND = 'locationPermissionsInsufficientForBackground', // The user has not chosen whether the app can use location services. SDK automatically asked for permissions.
  LOCATION_PERMISSIONS_NOT_DETERMINED = 'locationPermissionsNotDetermined', // [iOS only] The user didn’t grant precise location permissions or downgraded permissions to imprecise.
  LOCATION_PERMISSIONS_REDUCED_ACCURACY = 'locationPermissionsReducedAccuracy', // The app is in Provisional Always authorization state, which stops sending locations when app is in background.
  LOCATION_PERMISSIONS_PROVISIONAL = 'locationPermissionsProvisional', // [iOS only] The app is not authorized to use location services.
  LOCATION_PERMISSIONS_RESTRICTED = 'locationPermissionsRestricted', // [iOS only] The user disabled location services systemwide.
  LOCATION_SERVICES_DISABLED = 'locationServicesDisabled', // The device doesn't have location services.
  LOCATION_SERVICES_UNAVAILABLE = 'locationServicesUnavailable', // [Android only] Motion activity permissions denied after SDK’s initialization. Granting them will restart the app, so in effect, they are denied during this app’s session.
  MOTION_ACTIVITY_PERMISSIONS_DENIED = 'motionActivityPermissionsDenied', // The user has not chosen whether the app can use motion activity services. SDK automatically asked for permissions.
  MOTION_ACTIVITY_PERMISSIONS_NOT_DETERMINED = 'motionActivityPermissionsNotDetermined', // [iOS only] The user disabled motion services systemwide.
  MOTION_ACTIVITY_SERVICES_DISABLED = 'motionActivityServicesDisabled', // [iOS only] The device doesn't have motion activity services.
  MOTION_ACTIVITY_SERVICES_UNAVAILABLE = 'motionActivityServicesUnavailable', // [iOS only] The SDK is not collecting locations because it’s neither tracking nor available.
  NOT_RUNNING = 'notRunning', // The SDK started collecting locations and is waiting for OS location services to respond.
  STARTING = 'starting', // HyperTrack Publishable key is invalid.
  INVALID_PUBLISHABLE_KEY = 'invalidPublishableKey', // The SDK was remotely blocked from running.
  BLOCKED_FROM_RUNNING = 'blockedFromRunning',
}

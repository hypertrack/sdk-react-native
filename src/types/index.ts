/** The possible tracking states. */
export enum TrackingState {
  /** HyperTrack stopped tracking. */
  STOPPED = 'stopped',

  /** HyperTrack started tracking. */
  STARTED = 'started',
}

export type Location = {
  latitude: number;
  longitude: number;
};

export enum LocationError {
  // GPS satellites are not in view.
  GPS_SIGNAL_LOST = 'gpsSignalLost',
  // The user enabled mock location app while mocking locations is prohibited.
  LOCATION_MOCKED = 'locationMocked',
  // The user has not chosen whether the app can use location services. SDK can’t ask for permissions in background.
  LOCATION_PERMISSIONS_CANT_BE_ASKED_IN_BACKGROUND = 'locationPermissionsCantBeAskedInBackground',
  // The user denied location permissions.
  LOCATION_PERMISSION_DENIED = 'locationPermissionsDenied',
  // Can’t start tracking in background with When In Use location permissions. SDK will automatically start tracking when app will return to foreground.
  LOCATION_PERMISSIONS_INSUFFICIENT_FOR_BACKGROUND = 'locationPermissionsInsufficientForBackground',
  // The user has not chosen whether the app can use location services. SDK automatically asked for permissions.
  LOCATION_PERMISSIONS_NOT_DETERMINED = 'locationPermissionsNotDetermined',
  // The user didn’t grant precise location permissions or downgraded permissions to imprecise.
  LOCATION_PERMISSIONS_REDUCED_ACCURACY = 'locationPermissionsReducedAccuracy',
  // The app is not authorized to use location services.
  LOCATION_PERMISSIONS__RESTRICTED = 'locationPermissionsRestricted',
  // The user disabled location services systemwide.
  LOCATION_SERVICES_DISABLED = 'locationServicesDisabled',
  // The user has not chosen whether the app can use motion activity services. SDK can’t ask for permissions in background.
  MOTION_ACTIVITY_PERMISSIONS_CANT_BE_ASKED_IN_BACKGROUND = 'motionActivityPermissionsCantBeAskedInBackground',
  // Motion activity permissions denied after SDK’s initialization. Granting them will restart the app, so in effect, they are denied during this app’s session.
  MOTION_ACTIVITY_PERMISSIONS_DENIED = 'motionActivityPermissionsDenied',
  // The user has not chosen whether the app can use motion activity services. SDK automatically asked for permissions.
  MOTION_ACTIVITY_PERMISSIONS_NOT_DETERMINED = 'motionActivityPermissionsNotDetermined',
  // The user disabled motion services systemwide.
  MOTION_ACTIVITY_SERVICES_DISABLED = 'motionActivityServicesDisabled',
  // The SDK is not collecting locations because it’s either not tracking or is unavailable.
  NOT_RUNNING = 'notRunning',
  // The SDK started collecting locations and is waiting for OS location services to respond.
  STARTING = 'starting',
}

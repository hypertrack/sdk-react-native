// enum naming convention is ignored to make datatype sync
// across platforms easier
export enum HyperTrackError {
  /**
   * GPS satellites are not in view.
   */
  gpsSignalLost = 'gpsSignalLost',

  /**
   * The user enabled mock location app while mocking locations is prohibited.
   */
  locationMocked = 'locationMocked',

  /**
   * The user denied location permissions.
   */
  locationPermissionsDenied = 'locationPermissionsDenied',

  /**
   * Can’t start tracking in background with When In Use location permissions.
   * SDK will automatically start tracking when app will return to foreground.
   */
  locationPermissionsInsufficientForBackground = 'locationPermissionsInsufficientForBackground',

  /**
   * [iOS only] The user has not chosen whether the app can use location services.
   */
  locationPermissionsNotDetermined = 'locationPermissionsNotDetermined',

  /**
   * The user didn’t grant precise location permissions or downgraded permissions to imprecise.
   */
  locationPermissionsReducedAccuracy = 'locationPermissionsReducedAccuracy',

  /**
   * [iOS only] The app is in Provisional Always authorization state, which stops sending locations when app is in background.
   */
  locationPermissionsProvisional = 'locationPermissionsProvisional',

  /**
   * [iOS only] The app is not authorized to use location services.
   */
  locationPermissionsRestricted = 'locationPermissionsRestricted',

  /**
   * The user disabled location services systemwide.
   */
  locationServicesDisabled = 'locationServicesDisabled',

  /**
   * [Android only] The device doesn't have location services.
   */
  locationServicesUnavailable = 'locationServicesUnavailable',

  /**
   * [iOS only] The user has not chosen whether the app can use motion activity services.
   */
  motionActivityPermissionsNotDetermined = 'motionActivityPermissionsNotDetermined',

  /**
   * The user denied motion activity permissions.
   */
  motionActivityPermissionsDenied = 'motionActivityPermissionsDenied',

  /**
   * [iOS only] The user disabled motion services systemwide.
   */
  motionActivityServicesDisabled = 'motionActivityServicesDisabled',

  /**
   * [iOS only] The device doesn't have motion activity services.
   */
  motionActivityServicesUnavailable = 'motionActivityServicesUnavailable',

  /**
   * [iOS only] The app is not authorized to use motion activity services.
   */
  motionActivityPermissionsRestricted = 'motionActivityPermissionsRestricted',

  /**
   * Network connection is not available.
   */
  networkConnectionUnavailable = 'networkConnectionUnavailable',

  /**
   * The publishable key is invalid.
   */
  invalidPublishableKey = 'invalidPublishableKey',

  /**
   * The SDK was remotely blocked from running.
   */
  blockedFromRunning = 'blockedFromRunning',
}

// enum naming convention is ignored to make datatype sync
// across platforms easier
export enum HyperTrackError {
  /**
   * The SDK was remotely blocked from running.
   */
  blockedFromRunning = 'blockedFromRunning',

  /**
   * The publishable key is invalid.
   */
  invalidPublishableKey = 'invalidPublishableKey',

  /**
   * The user enabled mock location app while mocking locations is prohibited.
   */
  locationMocked = 'location.mocked',

  /**
   * The user disabled location services systemwide.
   */
  locationServicesDisabled = 'location.servicesDisabled',

  /**
   * [Android only] The device doesn't have location services.
   */
  locationServicesUnavailable = 'location.servicesUnavailable',

  /**
   * GPS satellites are not in view.
   */
  locationSignalLost = 'location.signalLost',

  /**
   * [Android only] The SDK wasn't able to start tracking because of the limitations imposed by the OS.
   * The exempt from background execution conditions weren't met.
   * {@link https://developer.android.com/guide/components/foreground-services#background-start-restriction-exemptions}
   */
  noExemptionFromBackgroundStartRestrictions = 'noExemptionFromBackgroundStartRestrictions',

  /**
   * The user denied location permissions.
   */
  permissionsLocationDenied = 'permissions.location.denied',

  /**
   * Can’t start tracking in background with When In Use location permissions.
   * SDK will automatically start tracking when app will return to foreground.
   */
  permissionsLocationInsufficientForBackground = 'permissions.location.insufficientForBackground',

  /**
   * [iOS only] The user has not chosen whether the app can use location services.
   */
  permissionsLocationNotDetermined = 'permissions.location.notDetermined',

  /**
   * [iOS only] The app is in Provisional Always authorization state, which stops sending locations when app is in background.
   */
  permissionsLocationProvisional = 'permissions.location.provisional',

  /**
   * The user didn't grant precise location permissions or downgraded permissions to imprecise.
   */
  permissionsLocationReducedAccuracy = 'permissions.location.reducedAccuracy',

  /**
   * [iOS only] The app is not authorized to use location services.
   */
  permissionsLocationRestricted = 'permissions.location.restricted',

  /**
   * [Android only] The user denied notification permissions needed to display a persistent notification
   * needed for foreground location tracking.
   */
  permissionsNotificationsDenied = 'permissions.notifications.denied',
}

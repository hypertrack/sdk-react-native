// enum naming convention is ignored to make datatype sync
// across platforms easier
export enum HyperTrackError {
  gpsSignalLost,
  locationMocked,
  locationPermissionsDenied,
  locationPermissionsInsufficientForBackground,
  locationPermissionsNotDetermined,
  locationPermissionsReducedAccuracy,
  locationPermissionsProvisional,
  locationPermissionsRestricted,
  locationServicesDisabled,
  locationServicesUnavailable,
  motionActivityPermissionsNotDetermined,
  motionActivityPermissionsDenied,
  motionActivityServicesDisabled,
  motionActivityServicesUnavailable,
  motionActivityPermissionsRestricted,
  networkConnectionUnavailable,
  invalidPublishableKey,
  blockedFromRunning
}

// enum naming convention is ignored to make datatype sync
// across platforms easier
export enum HyperTrackError {
  gpsSignalLost,
  locationPermissionsDenied,
  locationPermissionsInsufficientForBackground,
  locationServicesDisabled,
  motionActivityPermissionsDenied,
  invalidPublishableKey,
  permissionsDenied,
  blockedFromRunning,
}

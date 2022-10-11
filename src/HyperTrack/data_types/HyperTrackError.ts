// enum naming convention is ignored to make datatype sync
// across platforms easier
export enum HyperTrackError {
  gpsSignalLost = "gpsSignalLost",
  locationMocked = "locationMocked",
  locationPermissionsDenied = "locationPermissionsDenied",
  locationPermissionsInsufficientForBackground = "locationPermissionsInsufficientForBackground",
  locationPermissionsNotDetermined = "locationPermissionsNotDetermined",
  locationPermissionsReducedAccuracy = "locationPermissionsReducedAccuracy",
  locationPermissionsProvisional = "locationPermissionsProvisional",
  locationPermissionsRestricted = "locationPermissionsRestricted",
  locationServicesDisabled = "locationServicesDisabled",
  locationServicesUnavailable = "locationServicesUnavailable",
  motionActivityPermissionsNotDetermined = "motionActivityPermissionsNotDetermined",
  motionActivityPermissionsDenied = "motionActivityPermissionsDenied",
  motionActivityServicesDisabled = "motionActivityServicesDisabled",
  motionActivityServicesUnavailable = "motionActivityServicesUnavailable",
  motionActivityPermissionsRestricted = "motionActivityPermissionsRestricted",
  networkConnectionUnavailable = "networkConnectionUnavailable",
  invalidPublishableKey = "invalidPublishableKey",
  blockedFromRunning = "blockedFromRunning"
}

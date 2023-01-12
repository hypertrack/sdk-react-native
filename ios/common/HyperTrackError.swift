enum HyperTrackError: String {
    case gpsSignalLost
    case locationMocked
    case locationPermissionsDenied
    case locationPermissionsInsufficientForBackground
    case locationPermissionsNotDetermined
    case locationPermissionsReducedAccuracy
    case locationPermissionsProvisional
    case locationPermissionsRestricted
    case locationServicesDisabled
    case locationServicesUnavailable
    case motionActivityPermissionsNotDetermined
    case motionActivityPermissionsDenied
    case motionActivityServicesDisabled
    case motionActivityServicesUnavailable
    case motionActivityPermissionsRestricted
    case networkConnectionUnavailable
    case invalidPublishableKey
    case blockedFromRunning
}

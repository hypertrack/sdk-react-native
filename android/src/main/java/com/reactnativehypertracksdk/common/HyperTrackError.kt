package com.reactnativehypertracksdk.common

// enum naming convention is ignored to make datatype sync
// across platforms easier
@Suppress("EnumEntryName")
enum class HyperTrackError {
    gpsSignalLost,
    locationPermissionsDenied,
    locationPermissionsInsufficientForBackground,
    locationServicesDisabled,
    motionActivityPermissionsDenied,
    invalidPublishableKey,
    blockedFromRunning
}

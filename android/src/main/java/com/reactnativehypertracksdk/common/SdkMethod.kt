package com.hypertrack.sdk.flutter.common

// enum naming convention is ingored to make datatype sync
// across platforms easier
internal enum class SdkMethod {
    initialize,
    getDeviceId,
    getLocation,
    startTracking,
    stopTracking,
    setAvailability,
    setName,
    setMetadata,
    isTracking,
    isAvailable,
    addGeotag,
    sync
}

package com.reactnativehypertracksdk.common

/**
 * The list of available methods in the SDK API.
 * Enum naming convention is ignored to make datatype sync across platforms easier
 */
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

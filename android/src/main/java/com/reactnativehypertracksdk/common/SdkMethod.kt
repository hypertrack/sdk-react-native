package com.reactnativehypertracksdk.common

/**
 * The list of available methods in the SDK API.
 * It is used to match method calls in some wrappers (like Flutter)
 * Enum naming convention is ignored to make datatype sync across platforms easier.
 * Using Swift naming convention.
 */
internal enum class SdkMethod {
    addGeotag,
    getDeviceID,
    getErrors,
    getIsAvailable,
    getIsTracking,
    getLocation,
    getMetadata,
    getName,
    locate,
    setIsAvailable,
    setIsTracking,
    setMetadata,
    setName,
}

package com.reactnativehypertracksdk.common

/**
 * The list of available methods in the SDK API.
 * It is used to match method calls in some wrappers (like Flutter)
 * Enum naming convention is ignored to make datatype sync across platforms easier.
 * Using Swift naming convention.
 */
internal enum class SdkMethod {
    addGeotag,
    getAllowMockLocation,
    getDeviceID,
    getDynamicPublishableKey,
    getErrors,
    getIsAvailable,
    getIsTracking,
    getLocation,
    getMetadata,
    getName,
    getOrderIsInsideGeofence,
    getOrders,
    getWorkerHandle,
    locate,
    setAllowMockLocation,
    setDynamicPublishableKey,
    setIsAvailable,
    setIsTracking,
    setMetadata,
    setName,
    setWorkerHandle,
}

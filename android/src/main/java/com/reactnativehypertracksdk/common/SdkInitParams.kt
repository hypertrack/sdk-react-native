package com.reactnativehypertracksdk.common

import com.reactnativehypertracksdk.common.Serialization.parse

/**
 * SDK config params
 */
internal data class SdkInitParams(
    val publishableKey: String,
    val requireBackgroundTrackingPermission: Boolean,
    val loggingEnabled: Boolean,
    val allowMockLocations: Boolean
) {

    companion object {
        fun fromMap(map: Map<String, Any?>): Result<SdkInitParams> {
            return parse(map) {
                SdkInitParams(
                    publishableKey = it
                        .get<String>(KEY_PUBLISHABLE_KEY)
                        .getOrThrow(),
                    requireBackgroundTrackingPermission = it
                        .get<Boolean>(KEY_REQUIRE_BACKGROUND_TRACKING_PERMISSION)
                        .getOrThrow(),
                    loggingEnabled = it
                        .get<Boolean>(KEY_LOGGING_ENABLED)
                        .getOrThrow(),
                    allowMockLocations = it
                        .get<Boolean>(KEY_ALLOW_MOCK_LOCATIONS)
                        .getOrThrow()
                )
            }
        }

        private const val KEY_PUBLISHABLE_KEY =
            "publishableKey"
        private const val KEY_REQUIRE_BACKGROUND_TRACKING_PERMISSION =
            "requireBackgroundTrackingPermission"
        private const val KEY_LOGGING_ENABLED =
            "loggingEnabled"
        private const val KEY_ALLOW_MOCK_LOCATIONS =
            "allowMockLocations"
    }
}

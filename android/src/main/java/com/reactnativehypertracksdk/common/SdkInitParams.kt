package com.reactnativehypertracksdk.common

import com.reactnativehypertracksdk.common.Serialization.parse
import android.util.Log

/**
 * SDK config params
 */
internal data class SdkInitParams(
    val publishableKey: String,
    val requireBackgroundTrackingPermission: Boolean,
    val loggingEnabled: Boolean,
    val allowMockLocations: Boolean,
) {

    companion object {
        fun fromMap(map: Map<String, Any?>): Result<SdkInitParams> {
            Log.v(javaClass.simpleName, "fromMap 1")
            return parse(map) {
                Log.v(javaClass.simpleName, "parse 1")
                SdkInitParams(
                    publishableKey = it.get<String>(KEY_PUBLISHABLE_KEY).getOrThrow(),
                    requireBackgroundTrackingPermission = it.get<Boolean>(
                        KEY_REQUIRE_BACKGROUND_TRACKING_PERMISSION
                    ).getOrThrow(),
                    loggingEnabled = it.get<Boolean>(KEY_LOGGING_ENABLED).getOrThrow(),
                    allowMockLocations = it.get<Boolean>(KEY_ALLOW_MOCK_LOCATIONS).getOrThrow()
                )
            }.also {
                Log.v(javaClass.simpleName, "fromMap $it")
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



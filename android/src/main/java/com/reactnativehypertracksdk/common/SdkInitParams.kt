package com.reactnativehypertracksdk.common


internal data class SdkInitParams(
    val requireBackgroundTrackingPermission: Boolean,
    val loggingEnabled: Boolean,
    val allowMockLocations: Boolean,
) {

    companion object {
        fun fromMap(map: Map<String, Any>): Result<SdkInitParams> {
            return try {
                Success(
                    SdkInitParams(
                        requireBackgroundTrackingPermission =
                        map[KEY_REQUIRE_BACKGROUND_TRACKING_PERMISSION] as Boolean? ?: false,
                        loggingEnabled = map[KEY_LOGGING_ENABLED] as Boolean? ?: false,
                        allowMockLocations = map[KEY_ALLOW_MOCK_LOCATIONS] as Boolean? ?: false
                    )
                )
            } catch (e: Exception) {
                Failure(Exception(ERROR_INVALID_SDK_INIT_PARAMS))
            }
        }

        private const val KEY_REQUIRE_BACKGROUND_TRACKING_PERMISSION =
            "requireBackgroundTrackingPermission"
        private const val KEY_LOGGING_ENABLED =
            "loggingEnabled"
        private const val KEY_ALLOW_MOCK_LOCATIONS =
            "allowMockLocations"

        private const val ERROR_INVALID_SDK_INIT_PARAMS = "Invalid SDK init params"
    }

}



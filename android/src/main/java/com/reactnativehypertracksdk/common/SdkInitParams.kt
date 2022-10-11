package com.reactnativehypertracksdk.common

data class SdkInitParams(
  val requireBackgroundTrackingPermission: Boolean,
  val loggingEnabled: Boolean,
  val allowMockLocations: Boolean,
) {

  companion object {
      fun fromMap(map: Map<String, Boolean>): SdkInitParams {
        return SdkInitParams(
          requireBackgroundTrackingPermission =
            map[KEY_REQUIRE_BACKGROUND_TRACKING_PERMISSION] ?: false,
          loggingEnabled = map[KEY_LOGGING_ENABLED] ?: false,
          allowMockLocations = map[KEY_ALLOW_MOCK_LOCATIONS] ?: false
        )
      }
      private const val KEY_REQUIRE_BACKGROUND_TRACKING_PERMISSION =
        "requireBackgroundTrackingPermission"
      private const val KEY_LOGGING_ENABLED =
        "loggingEnabled"
      private const val KEY_ALLOW_MOCK_LOCATIONS =
        "allowMockLocations"
  }

}



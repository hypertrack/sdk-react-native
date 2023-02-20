/**
 * Parameters for initializing the SDK.
 */
export type SdkInitParams = {
  /**
   * Enable debug logging.
   * @default false
   */
  loggingEnabled?: boolean;

  /**
   * Allow mock locations to pass through and don’t send mock location outage.
   * @default false
   */
  allowMockLocations?: boolean;

  /**
   * Require Background location permissions while requiring permissions on Android.
   * @default false
   */
  requireBackgroundTrackingPermission?: boolean;

  /**
   * Automatically request permissions on starting tracking on iOS.
   * @default true
   */
  automaticallyRequestPermissions?: boolean;
};

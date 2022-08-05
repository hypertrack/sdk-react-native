import { NativeModules, Platform, NativeEventEmitter } from 'react-native';

const LINKING_ERROR =
  `The package 'hypertrack-sdk-react-native' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const HyperTrackSdk = NativeModules.HyperTrack
  ? NativeModules.HyperTrack
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

const EventEmitter = new NativeEventEmitter(HyperTrackSdk);

export enum SDKError {
  /**
   * An error that we couldn't recognize.
   */
  GENERAL = 0,
  /**
   * Publishable key is invalid. Check your key on hypertrack dashboard setup.
   */
  INVALID_PUBLISHABLE_KEY = 1,
  /**
   * Your free trial ended or account was not renewed. Check your account on hypertrack dashboard.
   */
  AUTHORIZATION_FAILED = 2,
  /**
   * Tracking won't start due to denied (or absent) permission.
   */
  PERMISSION_DENIED = 3,
}

/**
 * A base interface for errors in SDK.
 */
export type Error = {
  code: SDKError;
  message?: string;
};

/**
 * Interface for accessing adding geotag result.
 */
export enum GeotagError {
  /** Expected location is not supported on this platform */
  PLATFORM_NOT_SUPPORTED = 0,
  /** Expected vs actual location difference is greater then required */
  LOCATION_MISMATCH = 1,
  /** SDK can't get actual location from OS sensors */
  LOCATION_NOT_AVAILABLE = 2,
  /** Invalid arguments  */
  INVALID_ARGUMENTS = 3,
}

export interface Location {
  location: {
    latitude: number;
    longitude: number;
  };
}

export enum LocationError {
  // The user has not chosen whether the app can use location services.
  // SDK automatically asked for permissions.
  LOCATION_PERMISSIONS_NOT_DETERMINED = 'location_permissions_not_determined',
  // The user has not chosen whether the app can use location services.
  // SDK can't ask for permissions in background.
  LOCATION_PERMISSIONS_CANT_BE_ASKED_IN_BACKGROUND = 'location_permissions_cant_be_asked_in_background',
  // Can't start tracking in background with When In Use location
  // permissions. SDK will automatically start tracking when app will return
  // to foreground.
  LOCATION_PERMISSIONS_INSUFFICIENT_FOR_BACKGROUND = 'location_permissions_insufficient_for_background',
  // The app is not authorized to use location services.
  LOCATION_PERMISSIONS_RESTRICTED = 'location_permissions_restricted',
  // The user didn't grant precise location permissions or downgraded permissions to imprecise.
  LOCATION_PERMISSIONS_REDUCED_ACCURACY = 'location_permissions_reduced_accuracy',
  // The user denied location permissions.
  LOCATION_PERMISSIONS_DENIED = 'location_permissions_denied',
  // The user disabled location services systemwide.
  LOCATION_SERVICES_DISABLED = 'location_services_disabled',
  // The user has not chosen whether the app can use motion activity
  // services. SDK automatically asked for permissions.
  MOTION_ACTIVITY_PERMISSIONS_NOT_DETERMINED = 'motion_activity_permissions_not_determined',
  // The user has not chosen whether the app can use motion activity
  // services. SDK can't ask for permissions in background.
  MOTION_ACTIVITY_PERMISSIONS_CANT_BE_ASKED_IN_BACKGROUND = 'motion_activity_permissions_cant_be_asked_in_background',
  // Motion activity permissions denied after SDK's initialization. Granting
  // them will restart the app, so in effect, they are denied during this app's
  // session.
  MOTION_ACTIVITY_PERMISSIONS_DENIED = 'motion_activity_permissions_denied',
  // The user disabled motion services systemwide.
  MOTION_ACTIVITY_SERVICES_DISABLED = 'motion_activity_services_disabled',
  // The SDK is not collecting locations because it's either not tracking or is unavailable.
  NOT_RUNNING = 'not_running',
  // GPS satellites are not in view.
  GPS_SIGNAL_LOST = 'gps_signal_lost',
  // The SDK started collecting locations and is waiting for OS location services to respond.
  STARTING = 'starting',
  // The user enabled mock location app while mocking locations is prohibited.
  LOCATION_MOCKED = 'location_mocked',
}

export default class HyperTrack {
  constructor() {
    return this;
  }

  static async createInstance(
    publishableKey: string,
    automaticallyRequestPermissions = true
  ) {
    try {
      await HyperTrackSdk.initialize(
        publishableKey,
        false,
        automaticallyRequestPermissions
      );
      return new HyperTrack();
    } catch (error: any) {
      throw new Error(error.message);
    }
  }

  /**
   * Enables debug log output. This is very verbose, so you shouldn't use this for
   * production build.
   */
  static enableDebugLogging(enable: boolean) {
    HyperTrack.enableDebugLogging(enable);
  }

  /**
   * Returns a string which is used by HyperTrack to uniquely identify the user.
   */
  async getDeviceID(): Promise<string> {
    return HyperTrackSdk.getDeviceID();
  }

  /**
   * Device's availability for nearby search
   *
   * @returns true when is available or false when unavailable
   */
  isAvailable(): Promise<boolean> {
    return HyperTrackSdk.isAvailable();
  }

  /**
   * Allows you to set device availability for nearby search
   *
   * @param availability true when is available or false when unavailable
   */
  async setAvailability(availability: Boolean): Promise<void> {
    return HyperTrackSdk.setAvailability(availability);
  }

  /**
   * Determine whether the SDK is tracking the movement of the user.
   * @return {boolean} Whether the user's movement data is getting tracked or not.
   */
  isTracking(): Promise<boolean> {
    return HyperTrackSdk.isTracking();
  }

  /**
   * Update device state from server.
   */
  syncDeviceSettings() {
    HyperTrackSdk.syncDeviceSettings();
  }

  /**
   * Start or resume tracking location and activity events.
   */
  startTracking() {
    HyperTrackSdk.startTracking();
  }

  /**
   * Stops the SDK from listening to the user's movement updates and recording any data.
   */
  stopTracking() {
    HyperTrackSdk.stopTracking();
  }

  /// The current location of the user or an outage reason.
  getLocation(): Promise<LocationError | Location> {
    return HyperTrackSdk.getLocation();
  }

  /**
   * Listen for errors.
   *
   * @param listener (errors: {code, message}) => void
   * @returns EmitterSubscription
   * @example
   * ```js
   * const subscription = HyperTrack.subscribeToErrors(errors => {
   *   errors.forEach(error => {
   *     // ... error
   *   })
   * })
   *
   * // later, to stop listening
   * subscription.remove()
   * ```
   */
  subscribeToErrors(listener: (errors: Error) => void) {
    return EventEmitter.addListener('onTrackingErrorHyperTrack', listener);
  }

  /**
   * Listen for tracking changes.
   *
   * @param listener (isTracking: boolean) => void
   * @returns EmitterSubscription
   * @example
   * ```js
   * const subscription = HyperTrack.subscribeToTracking(isTracking => {
   *   if (isTracking) {
   *     // ... ready to go
   *   }
   * })
   *
   * // later, to stop listening
   * subscription.remove()
   * ```
   */
  subscribeToTracking(listener: (isTracking: boolean) => void) {
    return EventEmitter.addListener('onTrackingStateChanged', listener);
  }

  /**
   * Send device name to HyperTrack
   * @param {string} name - Device name you want to see in the Dashboard.
   */
  setDeviceName(name: string) {
    HyperTrackSdk.setDeviceName(name);
  }

  /**
   * Send metadata details to HyperTrack
   * @param {Object} data - Send extra device information.
   */
  setMetadata(data: Object) {
    HyperTrackSdk.setMetadata(data);
  }

  /**
   * @deprecated
   * Set a trip marker
   * @param {Object} _data - Include anything that can be parsed into JSON.
   */
  setTripMarker(_data: Object) {
    throw new Error('Deprecated');
  }

  /**
   * Add geotag
   * @param {Object} data - Include anything that can be parsed into JSON.
   */
  addGeotag(data: Object): Promise<LocationError | Location> {
    return HyperTrackSdk.addGeotag(data);
  }
}

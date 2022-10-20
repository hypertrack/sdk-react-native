import { NativeModules, Platform, NativeEventEmitter } from 'react-native';
import type {
  HyperTrackError,
  Location,
  LocationError,
  SDKDeviceID,
  SDKErrors,
  SDKIsAvailable,
  SDKIsTracking,
  SDKLocation,
  SDKLocationError,
} from 'src/types';
import {
  deserializeAvailability,
  deserializeDeviceID,
  deserializeHyperTrackErrorsResponse,
  deserializeIsTracking,
  deserializeLocationResponse,
} from './Serialization';

const EVENT_TRACKING = 'onTrackingChanged';
const EVENT_AVAILABILITY = 'onAvailabilityChanged';
const EVENT_ERROR = 'onError';

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
  static setLoggingEnabled(enable: boolean) {
    HyperTrackSdk.enableDebugLogging(enable);
  }

  /**
   * Returns a string which is used by HyperTrack to uniquely identify the user.
   */
  async getDeviceID(): Promise<string> {
    return HyperTrackSdk.getDeviceID().then((deviceIDResponse: SDKDeviceID) =>
      deserializeDeviceID(deviceIDResponse)
    );
  }

  /**
   * Device's availability for nearby search
   *
   * @returns true when is available or false when unavailable
   */
  isAvailable(): Promise<boolean> {
    return HyperTrackSdk.isAvailable().then(
      (isAvailableResponse: SDKIsAvailable) =>
        deserializeAvailability(isAvailableResponse)
    );
  }

  /**
   * Allows you to set device availability for nearby search
   *
   * @param availability true when is available or false when unavailable
   */
  setAvailability(availability: Boolean) {
    HyperTrackSdk.setAvailability(availability);
  }

  /**
   * Determine whether the SDK is tracking the movement of the user.
   * @return {boolean} Whether the user's movement data is getting tracked or not.
   */
  isTracking(): Promise<boolean> {
    return HyperTrackSdk.isTracking().then(
      (isTrackingResponse: SDKIsTracking) =>
        deserializeIsTracking(isTrackingResponse)
    );
  }

  /**
   * Update device state from server.
   */
  sync() {
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
    return HyperTrackSdk.getLocation().then(
      (locationResponse: SDKLocationError | SDKLocation) => {
        deserializeLocationResponse(locationResponse);
      }
    );
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
  subscribeToErrors(listener: (errors: HyperTrackError[]) => void) {
    return EventEmitter.addListener(EVENT_ERROR, (errorsResponse: SDKErrors) =>
      listener(deserializeHyperTrackErrorsResponse(errorsResponse))
    );
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
    return EventEmitter.addListener(
      EVENT_TRACKING,
      (isTrackingResponse: SDKIsTracking) => {
        listener(deserializeIsTracking(isTrackingResponse));
      }
    );
  }

  subscribeToAvailability(listener: (isAvailable: boolean) => void) {
    return EventEmitter.addListener(
      EVENT_AVAILABILITY,
      (isAvailableResponse) => {
        listener(deserializeAvailability(isAvailableResponse));
      }
    );
  }

  /**
   * Send device name to HyperTrack
   * @param {string} name - Device name you want to see in the Dashboard.
   */
  setName(name: string) {
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
   * Add geotag
   * @param {Object} data - Include anything that can be parsed into JSON.
   */
  addGeotag(data: Object): Promise<LocationError | Location> {
    return HyperTrackSdk.addGeotag(data).then(
      (locationResponse: SDKLocation) => {
        deserializeLocationResponse(locationResponse);
      }
    );
  }
}

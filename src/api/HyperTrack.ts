import {
  NativeModules,
  Platform,
  NativeEventEmitter,
  EmitterSubscription,
} from 'react-native';
import type {
  HyperTrackErrors,
  Location,
  LocationError,
  Metadata,
} from '../types';

const LINKING_ERROR =
  `The package 'react-native-hypertrack-sdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const HyperTrackSdk = NativeModules.HyperTrackSdk
  ? NativeModules.HyperTrackSdk
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

const EventEmitter = new NativeEventEmitter(HyperTrackSdk);

/**
 * A root class of the HyperTrack SDK. It is the default export of this library.
 *
 * @example
 * ```js
 * import HyperTrack from 'react-native-hypertrack-sdk'
 * ```
 */
export default class HyperTrack {
  /**
   * Gets the unique device ID
   *
   * @returns A string used to identify a device uniquely
   */
  static async getDeviceID(): Promise<string> {
    return HyperTrackSdk.getDeviceID();
  }

  /**
   * Gets device name from the dashboard
   *
   * @returns device name
   */
  static async getName(): Promise<string> {
    return HyperTrackSdk.getName();
  }

  /**
   * Allows you to set device name to be shown in dashboard
   *
   * @param name device name
   * @returns void
   */
  static async setName(name: string): Promise<void> {
    return HyperTrackSdk.setName(name);
  }

  /**
   * Gets metadata associated with device
   *
   * @returns metadata
   */
  static async getMetadata(): Promise<Metadata> {
    return HyperTrackSdk.getMetadata();
  }

  /**
   * Allows you to set metadata to be associated with device
   *
   * @param  metadata metadata to be associated with device
   */
  static async setMetadata(metadata: Metadata): Promise<void> {
    return HyperTrackSdk.setMetadata(metadata);
  }

  /**
   * Gets current latitude and longitude of device or LocationError
   *
   * @returns current latitude and longitude or LocationError
   */
  static async getLocation(): Promise<Location | LocationError> {
    return HyperTrackSdk.getLocation();
  }

  /**
   * Device's availability for nearby search
   *
   * @returns true when is available or false when unavailable
   */
  static async isAvailable(): Promise<boolean> {
    return HyperTrackSdk.isAvailable();
  }

  /**
   * Allows you to set device availability for nearby search
   *
   * @param availability true when is available or false when unavailable
   */
  static async setAvailability(availability: Boolean): Promise<void> {
    return HyperTrackSdk.setAvailability(availability);
  }

  /**
   * Reflects tracking intent
   *
   * @returns true when device is tracking or false when device is not tracking
   */
  static async isTracking(): Promise<boolean> {
    return HyperTrackSdk.isTracking();
  }

  /**
   * A list of errors that can prevent the app from successfuly tracking.
   *
   * @returns Returns a list of HyperTrackError
   */
  static async getErrors(): Promise<Array<HyperTrackErrors>> {
    return HyperTrackSdk.getErrors();
  }

  /**
   * Expresses an intent to start location tracking
   *
   * @returns void
   */
  static async startTracking(): Promise<void> {
    return HyperTrackSdk.startTracking();
  }

  /**
   * Expresses an intent to stop location tracking
   *
   * @returns void
   */
  static async stopTracking(): Promise<void> {
    return HyperTrackSdk.stopTracking();
  }

  /**
   * Tag a location with metadata
   *
   * @param metadata Metadata to be associated with geotag
   * @returns a location that the geotag was attached to as Result<Location, LocationError>
   */
  static async addGeotag(
    metadata: Metadata
  ): Promise<Location | LocationError> {
    return HyperTrackSdk.addGeotag(metadata);
  }

  /**
   * Manually synchronizes tracking and availability intents with the cloud
   *
   * @returns void
   */
  static async sync(): Promise<void> {
    return HyperTrackSdk.sync();
  }

  /**
   * Listen for location.
   *
   * @param listener (location: { latitude: number; longitude: number }) => void
   * @returns EmitterSubscription
   * @example
   * ```js
   * const subscription = HyperTrack.subscribeToLocation(location => {
   *   const { latitude, longitude } = location;
   * })
   *
   * // later, to stop listening
   * HyperTrack.clearSubscriptionToTracking(subscription)
   * ```
   */
  static subscribeToLocation(listener: (location: Location) => void) {
    HyperTrackSdk.subscribeToLocation();
    return EventEmitter.addListener('onLocationChanged', listener);
  }

  /**
   * Clears current location observers.
   * @param eventlistener to remove
   * @returns void
   */
  static clearSubscriptionToLocation(eventlistener: EmitterSubscription) {
    HyperTrackSdk.cancelSubscription('unsubscribeToLocation');
    return eventlistener.remove();
  }

  /**
   * Listen for availability.
   *
   * @param listener (isAvailable: boolean) => void
   * @returns EmitterSubscription
   * @example
   * ```js
   * const subscription = HyperTrack.subscribeToAvailability(isAvailable => {
   *   if (isAvailable) {
   *     // ... ready for nearby search.
   *   }
   * })
   *
   * // later, to stop listening
   * HyperTrack.clearSubscriptionToAvailability(subscription)
   * ```
   */
  static subscribeToAvailability(listener: (isAvailable: boolean) => void) {
    HyperTrackSdk.subscribeToAvailability();
    return EventEmitter.addListener('onAvailableStateChanged', listener);
  }

  /**
   * Clears current availability observers.
   * @param eventlistener to remove
   * @returns void
   */
  static clearSubscriptionToAvailability(eventlistener: EmitterSubscription) {
    HyperTrackSdk.cancelSubscription('unsubscribeToAvailability');
    eventlistener.remove();
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
   * HyperTrack.clearSubscriptionToTracking(subscription)
   * ```
   */
  static subscribeToTracking(listener: (isTracking: boolean) => void) {
    HyperTrackSdk.subscribeToTracking();
    return EventEmitter.addListener('onTrackingStateChanged', listener);
  }

  /**
   * Clears current tracking observers.
   * @param eventlistener to remove
   * @returns void
   */
  static clearSubscriptionToTracking(eventlistener: EmitterSubscription) {
    HyperTrackSdk.cancelSubscription('unsubscribeToTracking');
    eventlistener.remove();
  }

  /**
   * Listen for errors.
   *
   * @param listener (errors: [string]) => void
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
   * HyperTrack.clearSubscriptionToErrors(subscription)
   * ```
   */
  static subscribeToErrors(listener: (errors: HyperTrackErrors[]) => void) {
    HyperTrackSdk.subscribeToErrors();
    return EventEmitter.addListener('onErrors', listener);
  }

  /**
   * Clears current errors observers.
   * @param eventlistener to remove
   * @returns void
   */
  static clearSubscriptionToErrors(eventlistener: EmitterSubscription) {
    HyperTrackSdk.cancelSubscription('unsubscribeToErrors');
    eventlistener.remove();
  }

  /**
   * Allows you to cancel specific observer
   *
   * @param subscriptionName A subscription name
   * @returns void
   */
  static async cancelSubscription(subscriptionName: string): Promise<void> {
    return HyperTrackSdk.cancelSubscription(subscriptionName);
  }
}

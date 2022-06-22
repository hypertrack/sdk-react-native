import { NativeModules, Platform, NativeEventEmitter } from 'react-native';
import type { Location, LocationError, TrackingState } from '../types';

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
   * Listen for changes of the Tracking State.
   *
   * @example
   * ```js
   * const subscription = HyperTrack.onTrackingStateChanged(trackingState => {
   *   if (trackingState === 'started') {
   *     // ... ready to go
   *   }
   * })
   *
   * // later, to stop listening
   * subscription.remove()
   * ```
   */
  static onTrackingStateChanged(
    listener: (trackingState: TrackingState) => void
  ) {
    return EventEmitter.addListener('onTrackingStateChanged', listener);
  }

  static onLocationChanged(
    listener: (location: { latitude: number; longitude: number }) => void
  ) {
    return EventEmitter.addListener('onLocationChanged', listener);
  }

  static onErrors(listener: (errors: any) => void) {
    return EventEmitter.addListener('onErrors', listener);
  }

  static onIncrementIncreasedChanged(
    listener: (trackingState: number) => void
  ) {
    return EventEmitter.addListener('onIncrement', listener);
  }

  static onIncrementDecreasedChanged(
    listener: (trackingState: number) => void
  ) {
    return EventEmitter.addListener('onDecrement', listener);
  }

  static async increase(): Promise<number> {
    return HyperTrackSdk.increment();
  }
  static async decrease(): Promise<number> {
    return HyperTrackSdk.decrement();
  }

  /**
   *
   * @returns A string used to identify a device uniquely
   */
  static async getDeviceID(): Promise<string> {
    return HyperTrackSdk.getDeviceID();
  }

  /**
   *
   * @returns current latitude and longitude or location error or LocationError
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
    return HyperTrackSdk.availability();
  }

  /**
   * Expresses an intent to start location tracking
   */
  static async startTracking(): Promise<void> {
    return HyperTrackSdk.startTracking();
  }

  /**
   * Expresses an intent to stop location tracking
   */
  static async stopTracking(): Promise<void> {
    return HyperTrackSdk.stopTracking();
  }

  /**
   *
   *
   * @returns
   */
  static async isTracking(): Promise<boolean> {
    return HyperTrackSdk.isTracking();
  }

  /**
   *
   *
   * @returns
   */
  static async subscribeToLocation(): Promise<boolean> {
    return HyperTrackSdk.subscribeToLocation();
  }

  /**
   *
   *
   * @returns
   */
  static async subscribeToIsTracking(): Promise<boolean> {
    return HyperTrackSdk.subscribeToIsTracking();
  }

  /**
   *
   *
   * @returns
   */
  static async subscribeToErrors(): Promise<boolean> {
    return HyperTrackSdk.subscribeToErrors();
  }

  /**
   *
   *
   * @param  deviceName A device name to be shown in dashboard
   */
  static async setDeviceName(deviceName: string): Promise<void> {
    return HyperTrackSdk.setDeviceName(deviceName);
  }

  /**
   *
   *
   * @param  subscriptionName A device name to be shown in dashboard
   */
  static async cancelSubscription(subscriptionName: string): Promise<void> {
    return HyperTrackSdk.cancelSubscription(subscriptionName);
  }
}

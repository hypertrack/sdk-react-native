import { NativeModules, Platform, NativeEventEmitter } from 'react-native';
import type { LocationErrorType, LocationType, TrackingState } from '../types';

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

export default class HyperTrack {
  /**
   * Listen for changes of the Tracking State.
   *
   * @example
   * ```js
   * const subscription = HyperTrack.onTrackingStateChanged(trackingState => {
   *   if (trackingState === 'connected') {
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
    return EventEmitter.addListener(
      HyperTrackSdk.TRACKING_STATE_CHANGED,
      listener
    );
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
   * @returns current latitude and longitude or location error
   */
  static async getLocation(): Promise<LocationType | LocationErrorType> {
    return HyperTrackSdk.getDeviceID();
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
  static async isRunning(): Promise<boolean> {
    return HyperTrackSdk.isRunning();
  }

  /**
   *
   *
   * @param  deviceName A device name to be shown in dashboard
   */
  static async setDeviceName(deviceName: string): Promise<void> {
    return HyperTrackSdk.setDeviceName(deviceName);
  }
}

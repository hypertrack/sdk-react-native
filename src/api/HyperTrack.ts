import { NativeModules, Platform, NativeEventEmitter } from 'react-native';
import type { TrackingState } from '../types';

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


const EventEmitter = new NativeEventEmitter(HyperTrackSdk)

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
    static onTrackingStateChanged(listener: (trackingState: TrackingState) => void) {
      return EventEmitter.addListener(HyperTrackSdk.TRACKING_STATE_CHANGED, listener)
    }

    static onIncrementIncreasedChanged(listener: (trackingState: number) => void) {
      return EventEmitter.addListener('onIncrement', listener)
    }

    static onIncrementDecreasedChanged(listener: (trackingState: number) => void) {
      return EventEmitter.addListener('onDecrement', listener)
    }

    static async increase(): Promise<number> {
      return HyperTrackSdk.increment()
    }
    static async decrease(): Promise<number> {
      return HyperTrackSdk.decrement()
    }
  }

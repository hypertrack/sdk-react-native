import { NativeModules, Platform, NativeEventEmitter } from 'react-native';
import { HyperTrackError } from './data_types/HyperTrackError';
import type { IsAvailable } from './data_types/internal/IsAvailable';
import type { IsTracking } from './data_types/internal/IsTracking';
import type { LocationResponse } from './data_types/internal/LocationResponse';
import type { Location } from './data_types/Location';
import type { LocationError } from './data_types/LocationError';
import type { SdkInitParams } from './data_types/SdkInitParams';
import type { DeviceId } from './data_types/internal/DeviceId';
import type { Geotag } from './data_types/internal/Geotag';
import type { DeviceName } from './data_types/internal/DeviceName';
import type { HyperTrackErrorInternal } from './data_types/internal/HyperTrackErrorInternal';

const EVENT_TRACKING = 'onTrackingChanged';
const EVENT_AVAILABILITY = 'onAvailabilityChanged';
const EVENT_ERROR = 'onError';

const LINKING_ERROR =
  `The package 'hypertrack-sdk-react-native' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const HyperTrackSdk = NativeModules.HyperTrackReactNativePlugin
  ? NativeModules.HyperTrackReactNativePlugin
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
  private constructor() {
    return this;
  }

  /**
   * Creates an SDK instance
   * 
   * @param publishableKey publishable key provided in HyperTrack’s dashboard setup page
   * @param sdkInitParams
   * @returns HyperTrack instance
   */
  static async initialize(
    publishableKey: string,
    sdkInitParams: SdkInitParams = {}
  ): Promise<HyperTrack> {
    try {
      await HyperTrackSdk.initializeSdk({
        publishableKey,
        loggingEnabled: sdkInitParams.loggingEnabled ?? false,
        allowMockLocations: sdkInitParams.allowMockLocations ?? false,
        requireBackgroundTrackingPermission: sdkInitParams.requireBackgroundTrackingPermission ?? false,
      });
      return new HyperTrack();
    } catch (error: any) {
      throw new Error(error.message);
    }
  }

  /**
   * Returns a string that is used to uniquely identify the device
   */
  getDeviceId(): Promise<string> {
    return HyperTrackSdk.getDeviceId().then(
      (deviceId: DeviceId) => deviceId.value
    );
  }

  /**
   * Reflects availability of the device for the Nearby search
   *
   * @returns true when is available or false when unavailable
   */
  isAvailable(): Promise<boolean> {
    return HyperTrackSdk.isAvailable().then(
      (isAvailable: IsAvailable) => isAvailable.value
    );
  }

  /**
   * Sets the availability of the device for the Nearby search
   *
   * @param availability true when is available or false when unavailable
   */
  setAvailability(isAvailable: boolean) {
    HyperTrackSdk.setAvailability({
      type: 'isAvailable',
      value: isAvailable
    } as IsAvailable);
  }

  /**
   * Reflects the tracking intent for the device
   * 
   * @return {boolean} Whether the user's movement data is getting tracked or not.
   */
  isTracking(): Promise<boolean> {
    return HyperTrackSdk.isTracking().then(
      (isTracking: IsTracking) => isTracking.value
    );
  }

  /**
   * Syncs the device state with the HyperTrack servers
   */
  sync() {
    HyperTrackSdk.sync();
  }

  /**
   * Expresses an intent to start location tracking for the device
   */
  startTracking() {
    HyperTrackSdk.startTracking();
  }

  /**
   * Stops location tracking immediately
   */
  stopTracking() {
    HyperTrackSdk.stopTracking();
  }

  /**
   * Reflects the current location of the user or an outage reason
   */
  getLocation(): Promise<LocationError | Location> {
    return HyperTrackSdk.getLocation().then(
      (locationResponse: LocationResponse) => {
        return this.deserializeLocationResponse(locationResponse);
      }
    );
  }

  /**
   * Sets the name for the device
   * @param {string} name
   */
  setName(name: string) {
    HyperTrackSdk.setName({
      type: 'deviceName',
      value: name
    } as DeviceName)
  }

  /**
   * Sets the metadata for the device
   * @param {Object} data - Metadata JSON
   */
  setMetadata(data: Object) {
    HyperTrackSdk.setMetadata(data);
  }

  /**
   * Adds a new geotag
   * @param {Object} data - Geotad data JSON
   * @returns current location if success or LocationError if failure
   */
  addGeotag(data: Object): Promise<LocationError | Location> {
    return HyperTrackSdk.addGeotag({
      data
    } as Geotag).then(
      (locationResponse: LocationResponse) => {
        return this.deserializeLocationResponse(locationResponse);
      }
    );
  }

  /**
   * Subscribe to tracking errors
   *
   * @param listener
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
    return EventEmitter.addListener(EVENT_ERROR, (rawErrors: HyperTrackErrorInternal[]) => {
      listener(this.deserializeHyperTrackErrors(rawErrors))
    });
  }

  /**
   * Subscribe to tracking intent changes
   *
   * @param listener
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
      (isTracking: IsTracking) => {
        listener(isTracking.value);
      }
    );
  }

  /**
   * Subscribe to availability changes
   *
   * @param listener
   * @returns EmitterSubscription
   * @example
   * ```js
   * const subscription = HyperTrack.subscribeToAvailability(isAvailable => {
   *   if (isAvailable) {
   *     // ... ready to go
   *   }
   * })
   *
   * // later, to stop listening
   * subscription.remove()
   * ```
   */
  subscribeToAvailability(listener: (isAvailable: boolean) => void) {
    return EventEmitter.addListener(
      EVENT_AVAILABILITY,
      (isAvailable: IsAvailable) => {
        listener(isAvailable.value);
      }
    );
  }

  /** @ignore */
  private deserializeHyperTrackErrors(errors: HyperTrackErrorInternal[]): HyperTrackError[] {
    return errors.map((error: HyperTrackErrorInternal) => HyperTrackError[error.value as keyof typeof HyperTrackError])
  }

  /** @ignore */
  private deserializeLocationResponse(response: LocationResponse): Location | LocationError {
    switch(response.type) {
      case "success":
        return response.value
      case "failure":
        switch(response.value.type) {
          case "notRunning":
          case "starting":
            return response.value
          case "errors":
            return {
              type: "errors",
              value: this.deserializeHyperTrackErrors(response.value.value)
            }
        }
    };
  }
}

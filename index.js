import { NativeModules } from "react-native";

const { RNHyperTrack } = NativeModules;

module.exports = {
  /**
   * Initialize the HyperTrack SDK
   * @param {string} publishableKey - A unique String that is used to identify your account with HyperTrack.
   * @param {bool} startsTracking - Should the SDK start tracking immediately after initialization
   */
  initialize(publishableKey) {
    RNHyperTrack.initialize(publishableKey, startsTracking);
  },
  /**
   * Determine whether the SDK is tracking the movement of the user.
   * @return {boolean} Whether the user's movement data is getting tracked or not.
   */
  isTracking() {
    return RNHyperTrack.isTracking();
  },
  /**
   * Returns a string which is used by HyperTrack to uniquely identify the user.
   */
  getDeviceID() {
    return RNHyperTrack.getDeviceID();
  },
  /**
   * Start or resume tracking location and activity events.
   */
  startTracking() {
    return RNHyperTrack.startTracking();
  },
  /**
   * Stops the SDK from listening to the user's movement updates and recording any data.
   */
  stopTracking() {
    RNHyperTrack.stopTracking();
  },
  /**
   * Send device name and metadata details to HyperTrack
   * @param {string} name - Device name you want to see in the Dashboard.
   * @param {ReadableMap} rMap - Send extra device information.
   */
  setDevice(name, rMap) {
    RNHyperTrack.setDevice(name, rMap);
  },
  /**
   * Set a trip marker
   * @param {ReadableMap} rMap - Include anything that can be parsed into JSON.
   */
  setTripMarker(rMap) {
    RNHyperTrack.setTripMarker(rMap);
  }
};
